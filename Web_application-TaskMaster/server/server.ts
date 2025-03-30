import WebSocket, { RawData } from 'ws';
import express from 'express';
import http from 'http';
import { Db, MongoClient, ObjectId } from 'mongodb';

// Express alkalmazás beállítása
const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

// Important to exclude the connection string in public source-code
const connection = new MongoClient('');
let db: Db;
connectToDB();

/**
 * Connects to the MongoDB database.
 */
async function connectToDB() {
    try {
        await connection.connect();
        db = connection.db('taskmaster');
        console.log("Sikeres adatbázis csatlakozás");
    } catch (error) {
        console.error("Failed to connect to the database:", error);
    }
}

// WebSocket kapcsolat kezelése
wss.on('connection', (ws) => {
    console.log('New connection');

    // A klienstől kapott üzenetek feldolgozása
    ws.on('message', async (message) => {
        try {
            const packet = JSON.parse(message.toString());
            console.log(packet); // DEBUG
            let user: UserDto | null = null;
            let project: ProjectDto | null = null;
            let projects: ProjectDto[] | null = null;
            let task: TaskDto | null = null;
            let tasks: TaskDto[] | null = null;
            let memberIcons: string[] | null = null;
            let comments: CommentDto[] | null = null;
            let notifications: NotificationDto[] | null = null;
            let allTask: TaskDto[] | null = null;
            let commentCount: number | null = null;

            switch (packet.type) {
                // Bejelentkezéskor elküldjük a fiókadatokat, projekteket, értesítéseket
                // Ellenőrizni kell a helyes bemenetet
                case 'login':
                    if(packet.username.length < 5 || packet.password.length < 5){
                        ws.send(JSON.stringify({ type: 'error', message: 'Username and password must not be less than 5 characters' }));
                        break;
                    }
                    else if(packet.username.length > 30 || packet.password.length > 30){
                        ws.send(JSON.stringify({ type: 'error', message: 'Username and password must not be more than 30 characters' }));
                        break;
                    }
                    user = await login(packet.username, packet.password);
                    if (user) {
                        ws.send(JSON.stringify({ type: 'user', user: user }));
                        projects = await getProjectsByUserId(user.id);
                        ws.send(JSON.stringify({ type: 'projects', projects: projects }));
                        allTask = await getAllTask(user.name);
                        commentCount = await getCommentCount(user.name);
                        ws.send(JSON.stringify({ type: 'statistics', allTask: allTask, commentCount: commentCount }));
                        notifications = await getNotifications(user.name);
                        ws.send(JSON.stringify({ type: 'notifications', notifications: notifications }));
                    } else {
                        ws.send(JSON.stringify({ type: 'error', message: 'Invalid password or username' }));
                    }
                    break;
                // Regisztrációkor nem küldjük el az adatokat csak a hibát, ha már létezik a felhasználó vagy valamilyen adat nem megfelelő
                case 'register':
                    if(packet.username.length < 5 || packet.password.length < 5 || packet.email.length < 5){
                        ws.send(JSON.stringify({ type: 'error', message: 'Credentials must not be less than 5 characters' }));
                        break;
                    }
                    else if(packet.username.length > 30 || packet.password.length > 30 || packet.email.length > 30){
                        ws.send(JSON.stringify({ type: 'error', message: 'Credentials must not be more than 30 characters' }));
                        break;
                    }
                    user = await register(packet.username, packet.email, packet.password);
                    if (!user) {
                        ws.send(JSON.stringify({ type: 'error', message: 'Username already taken' }));
                    }
                    break;
                // Felhasználó adatainak módosítása
                // Elküldjük a módosult felhasználói adadtot
                case 'edit_user':
                    if(packet.name.length < 5 || packet.email.length < 5){
                        ws.send(JSON.stringify({ type: 'error', message: 'Username and email must not be less than 5 characters' }));
                        break;
                    }
                    if(packet.name.length > 30 || packet.email.length > 30){
                        ws.send(JSON.stringify({ type: 'error', message: 'Username and email must not be more than 30 characters' }));
                        break;
                    }
                    user = await editUser(packet.id, packet.name, packet.email, packet.picture, packet.theme);
                    ws.send(JSON.stringify({ type: 'user', user: user }));
                    // --> username változtatás nem lehetséges így nem kell foglalkozni azzal hogy máshol ez a referencia
                    break;
                // Projekt létrehozása után visszaadjuk az így kapott projekteket
                case 'create_project':
                    projects = await createProject(packet.name, packet.description, packet.creator);
                    ws.send(JSON.stringify({ type: 'projects', projects: projects }));
                    break;
                // Projekt megnyitása után visszaadjuk a projekthez tartozó feladatokat és projekttagokat
                case 'open_project':
                    console.log(packet.id+" project see tasks");
                    project = await getProject(packet.id);
                    ws.send(JSON.stringify({ type: 'project', project: project }));
                    tasks = await getTasks(packet.id);
                    ws.send(JSON.stringify({ type: 'tasks', tasks: tasks }));
                    memberIcons = await getMemberIcons(packet.id);
                    ws.send(JSON.stringify({ type: 'members', members: memberIcons }));
                    break;
                // Projekt törlése után visszaadjuk a felhasználó összes projektjét
                // Nincs implementálva kliens oldalon
                case 'delete_project':
                    projects = await deleteProject(packet.id, packet.member);
                    if(projects) {
                        ws.send(JSON.stringify({ type: 'projects', projects: projects }));
                    } else {
                        ws.send(JSON.stringify({ type: 'error', message: 'Deletion not allowed' }));
                    }
                    break;
                // Nincs implementálva kliens oldalon
                case 'quit_project':
                    projects = await quitProject(packet.p_id, packet.m_id);
                    ws.send(JSON.stringify({ type: 'projects', projects: projects }));
                    break;
                // Feladat létrehozása után visszaadjuk az így kapott feladatokat
                case 'create_task':
                    tasks = await createTask(packet.project, packet.title, packet.description, packet.status, packet.deadline, packet.editor);
                    ws.send(JSON.stringify({ type: 'tasks', tasks: tasks }));
                    break;
                // Feladat megnyitása után visszaadjuk a feladatot és a hozzátartozó kommenteket
                case 'open_task':
                    task = await getTask(packet.id);
                    ws.send(JSON.stringify({ type: 'task', task: task }));
                    comments = await getComments(packet.id);
                    ws.send(JSON.stringify({ type: 'comments', comments: comments }));
                    break;
                // Feladat módosítása után visszaadjuk az így kapott feladatot és a projekt összes feladatát
                case 'edit_task':
                    task = await editTask(packet.id, packet.project, packet.title, packet.description, packet.status, packet.deadline, packet.editor);
                    ws.send(JSON.stringify({ type: 'task', task: task}));
                    tasks = await getTasks(packet.project);
                    ws.send(JSON.stringify({ type: 'tasks', tasks: tasks }));
                    break;
                // Feladat törlése után visszaadjuk a projekt összes feladatát
                // Nincs implementálva kliens oldalon
                case 'delete_task':
                    tasks = await deleteTask(packet.id);
                    ws.send(JSON.stringify({ type: 'tasks', tasks: tasks }));
                    break;
                // Tag hozzáadása után visszaadjuk a projektet és a tagok ikonjait
                case 'add_member':
                    project = await addMember(packet.project, packet.user);
                    if(project) {
                        ws.send(JSON.stringify({ type: 'project', project: project }));
                        memberIcons = await getMemberIcons(packet.project);
                        ws.send(JSON.stringify({ type: 'members', members: memberIcons }));
                    } else {
                        ws.send(JSON.stringify({ type: 'error', message: 'User does not exist' }));
                    }
                    break;
                // Komment létrehozása után visszaadjuk a kommenteket
                case 'create_comment':
                    comments = await createComment(packet.task, packet.user, packet.content);
                    ws.send(JSON.stringify({ type: 'comments', comments: comments }));
                    break;
                // Értesítés létrehozása után visszaadjuk az értesítéseket
                case 'create_notification':
                    notifications = await createNotification(packet.user, packet.task, packet.message, packet.notification_type, packet.date);
                    ws.send(JSON.stringify({ type: 'notifications', notifications: notifications }));
                    break;
                // Értesítés törlése után visszaadjuk az így megmaradt értesítéseket
                case 'delete_notification':
                    notifications = await deleteNotification(packet.id);
                    ws.send(JSON.stringify({ type: 'notifications', notifications: notifications }));
                    break;
                // Statisztikák lekérdezése után visszaadjuk az összes feladatot és a kommentek számát, valamint az értesítéseket is (mert nem volt kedvem mégegy packet-et definiálni)
                case 'get_statistics':
                    allTask = await getAllTask(packet.user);
                    commentCount = await getCommentCount(packet.user);
                    ws.send(JSON.stringify({ type: 'statistics', allTask: allTask, commentCount: commentCount }));
                    projects = await getProjectsByUsername(packet.user);
                    ws.send(JSON.stringify({ type: 'projects', projects: projects }));
                    notifications = await getNotifications(packet.user);
                    ws.send(JSON.stringify({ type: 'notifications', notifications: notifications }));
                    break;
                // Ha nem megfelelő formátumú packet érkezik akkor hibát küldünk
                default:
                    console.error('Unknown packet type:', packet.type);
            }
        } catch (error) {
            console.error('Error handling message:', error);
            ws.send(JSON.stringify({ type: 'error', message: 'Invalid JSON format' }));
            return; // A hiba után térj vissza
        }
    });

    ws.on('close', () => {
        console.log('Client disconnected');
    });
});

// WebSocket szerver futtatása
server.listen(8080, () => {
    console.log('Server runs on http://localhost:8080/');
});

/**
 * Logs in a user by username and password.
 * @param username - The username of the user.
 * @param password - The password of the user.
 * @returns A promise that resolves to a UserDto object or null if the login fails.
 */
async function login(username: string, password: string): Promise<UserDto | null> {
    // Search for the user by username and password
    const user = await db.collection('users').findOne({ name: username, password: password });

    if (!user) {
        console.log("Invalid username or password");
        return null; // User not found or credentials are invalid
    }

    // Convert the retrieved user data into UserDTO format
    const userDto: UserDto = {
        id: user._id.toHexString(), // Convert MongoDB ObjectId to string for UserDto
        name: user.name,
        email: user.email,
        password: user.password,
        picture: user.picture,
        theme: user.theme,
        created: user.created
    };

    return userDto;
}

/**
 * Registers a new user.
 * @param username - The username of the new user.
 * @param email - The email of the new user.
 * @param password - The password of the new user.
 * @returns A promise that resolves to a UserDto object or null if the registration fails.
 */
async function register(username: string, email: string, password: string): Promise<UserDto | null> {
    // Check if user already exists
    const existingUser = await db.collection('users').findOne({ name: username });
    if (existingUser) {
        console.log("User already exists with this username");
        return null;
    }

    // Create the UserDTO object
    const newUser: UserDto = {
        id: new ObjectId().toHexString(), // Generated unique ID
        name: username,
        email: email,
        password: password,
        picture: 'default', // Default picture path, if any
        theme: 'dark', // Default theme, set to dark
        created: new Date().getTime().toString()
    };

    // Insert the user and return the result
    await db.collection('users').insertOne(newUser);
    return newUser;
}

/**
 * Edits an existing user.
 * @param id - The ID of the user to edit.
 * @param name - The new name of the user.
 * @param email - The new email of the user.
 * @param picture - The new picture of the user.
 * @param theme - The new theme of the user.
 * @returns A promise that resolves to a UserDto object or null if the update fails.
 */
async function editUser(id: string, name: string, email: string, picture: string, theme: string): Promise<UserDto | null> {
    const updatedUser = await db.collection('users').findOneAndUpdate(
        { _id: new ObjectId(id) },
        { $set: { name, email, picture, theme } },
        { returnDocument: 'after' }
    );

    if (!updatedUser.value) {
        console.log("User not found or update failed");
        return null;
    }

    return {
        id: updatedUser.value._id.toHexString(),
        name: updatedUser.value.name,
        email: updatedUser.value.email,
        password: updatedUser.value.password,
        picture: updatedUser.value.picture,
        theme: updatedUser.value.theme,
        created: updatedUser.value.created
    };
}

/**
 * Creates a new project.
 * @param name - The name of the project.
 * @param description - The description of the project.
 * @param creator - The username of the project creator.
 * @returns A promise that resolves to an array of ProjectDto objects.
 */
async function createProject(name: string, description: string, creator: string): Promise<ProjectDto[]> {
    const newProject = {
        _id: new ObjectId(),
        name,
        description,
        creator,
        created: new Date().getTime().toString(),
        members: [creator]
    };

    await db.collection('projects').insertOne(newProject);
    return await getProjectsByUsername(creator);
}

/**
 * Gets projects by username.
 * @param username - The username to search for.
 * @returns A promise that resolves to an array of ProjectDto objects.
 */
async function getProjectsByUsername(username: string): Promise<ProjectDto[]> {
    const projects = await db.collection('projects').find({ members: username }).toArray();
    return projects.map(project => ({
        id: project._id.toHexString(),
        name: project.name,
        description: project.description,
        creator: project.creator,
        created: project.created,
        members: project.members
    }));
}

/**
 * Gets projects by user ID.
 * @param userId - The ID of the user to search for.
 * @returns A promise that resolves to an array of ProjectDto objects.
 */
async function getProjectsByUserId(userId: string): Promise<ProjectDto[]> {
    const user = await db.collection('users').findOne({ _id: new ObjectId(userId) });
    if (!user) {
        console.log("User not found");
        return [];
    }
    const username = user.name;
    return await getProjectsByUsername(username);
}

/**
 * Gets a project by its ID.
 * @param projectId - The ID of the project to search for.
 * @returns A promise that resolves to a ProjectDto object or null if the project is not found.
 */
async function getProject(projectId: string): Promise<ProjectDto | null> {
    const project = await db.collection('projects').findOne({ _id: new ObjectId(projectId) });

    return project ? {
        id: project._id.toHexString(),
        name: project.name,
        description: project.description,
        creator: project.creator,
        created: project.created,
        members: project.members
    } : null;
}

/**
 * Deletes a project by its ID.
 * @param projectId - The ID of the project to delete.
 * @param memberId - The ID of the member requesting the deletion.
 * @returns A promise that resolves to an array of ProjectDto objects or null if the deletion fails.
 */
async function deleteProject(projectId: string, memberId: string): Promise<ProjectDto[] | null> {
    const result = await db.collection('projects').deleteOne({ _id: new ObjectId(projectId) });

    if (result.deletedCount === 0) {
        console.log("Project not found or delete not allowed");
    }

    deleteTasks(projectId);
    return getProjectsByUserId(memberId);
}

/**
 * Adds a member to a project.
 * @param projectId - The ID of the project.
 * @param username - The username of the member to add.
 * @returns A promise that resolves to a ProjectDto object or null if the addition fails.
 */
async function addMember(projectId: string, username: string): Promise<ProjectDto | null> {

    const user = await db.collection('users').findOne({ name: username });
    if (!user) {
        console.log("User not found");
        return null;
    }

    const project = await db.collection('projects').findOne({ _id: new ObjectId(projectId) });

    if (!project) {
        console.log("Project not found");
        return null;
    }

    if(!project.members.includes(user.name)) {
        project.members.push(user.name);
        await db.collection('projects').updateOne({ _id: new ObjectId(projectId) }, { $set: { members: project.members } });
    }

    return {
        id: project._id.toHexString(),
        name: project.name,
        description: project.description,
        creator: project.creator,
        created: project.created,
        members: project.members
    }
}

/**
 * Removes a member from a project.
 * @param projectId - The ID of the project.
 * @param memberId - The ID of the member to remove.
 * @returns A promise that resolves to an array of ProjectDto objects or null if the removal fails.
 */
async function quitProject(projectId: string, memberId: string): Promise<ProjectDto[] | null> {
    const project = await db.collection('projects').findOne({ _id: new ObjectId(projectId) });

    if (!project) {
        console.log("Project not found");
        return getProjectsByUserId(memberId);
    }

    const memberIndex = project.members.indexOf(memberId);
    if (memberIndex === -1) {
        console.log("Member not found in project");
        return getProjectsByUserId(memberId);
    }

    project.members.splice(memberIndex, 1);

    await db.collection('projects').updateOne({ _id: new ObjectId(projectId) }, { $set: { members: project.members } });

    return getProjectsByUserId(memberId);
}

/**
 * Creates a new comment for a task.
 * @param task - The ID of the task.
 * @param user - The username of the user creating the comment.
 * @param content - The content of the comment.
 * @returns A promise that resolves to an array of CommentDto objects.
 */
async function createComment(task: string, user: string, content: string): Promise<CommentDto[]> {
    const newComment = {
        _id: new ObjectId(),
        task,
        user,
        content,
        created: new Date().getTime().toString()
    };

    await db.collection('comments').insertOne(newComment);
    return await getComments(task);
}

/**
 * Gets comments for a task.
 * @param taskId - The ID of the task.
 * @returns A promise that resolves to an array of CommentDto objects.
 */
async function getComments(taskId: string): Promise<CommentDto[]> {
    const comments = await db.collection('comments').find({ task: taskId }).toArray();

    return comments.map(comment => ({
        id: comment._id.toHexString(),
        task: comment.task,
        user: comment.user,
        content: comment.content,
        created: comment.created
    }));
}

/**
 * Creates a new notification for a user.
 * @param user - The username of the user.
 * @param task - The ID of the task.
 * @param message - The message of the notification.
 * @param notificationType - The type of the notification.
 * @param date - The date of the notification.
 * @returns A promise that resolves to an array of NotificationDto objects.
 */
async function createNotification(user: string, task: string, message: string, notificationType: string, date: string): Promise<NotificationDto[]> {
    
    const newNotification = {
        _id: new ObjectId(),
        user,
        task,
        message,
        type: notificationType,
        date
    };

    await db.collection('notifications').insertOne(newNotification);
    return getNotifications(user);
}

/**
 * Gets notifications for a user.
 * @param username - The username to search for.
 * @returns A promise that resolves to an array of NotificationDto objects.
 */
async function getNotifications(username: string): Promise<NotificationDto[]> {
    const notifications = await db.collection('notifications').find({ user: username }).toArray();

    return notifications.map(notification => ({
        id: notification._id.toHexString(),
        user: notification.user,
        task: notification.task,
        message: notification.message,
        type: notification.type,
        date: notification.date
    }));
}

/**
 * Deletes a notification by its ID.
 * @param notificationId - The ID of the notification to delete.
 * @returns A promise that resolves to an array of NotificationDto objects or null if the deletion fails.
 */
async function deleteNotification(notificationId: string): Promise<NotificationDto[]| null> {
    const notification = await db.collection('notifications').findOne({ _id: new ObjectId(notificationId) });

    if (!notification) {
        console.log("Notification not found");
        return null;
    }

    await db.collection('notifications').deleteOne({ _id: new ObjectId(notificationId) });

    return getNotifications(notification.user);
}

/**
 * Creates a new task.
 * @param project - The ID of the project the task belongs to.
 * @param title - The title of the task.
 * @param description - The description of the task.
 * @param status - The status of the task.
 * @param deadline - The deadline of the task.
 * @param editor - The username of the task editor.
 * @returns A promise that resolves to an array of TaskDto objects.
 */
async function createTask(project: string, title: string, description: string, status: string, deadline: string, editor: string): Promise<TaskDto[]> {
    const newTask = {
        _id: new ObjectId(),
        project,
        title,
        description,
        status,
        deadline,
        editor,
        updated: new Date().getTime().toString()
    };

    await db.collection('tasks').insertOne(newTask);
    return getTasks(project);
}

/**
 * Gets tasks by project ID.
 * @param projectId - The ID of the project to search for.
 * @returns A promise that resolves to an array of TaskDto objects.
 */
async function getTasks(projectId: string): Promise<TaskDto[]> {
    const tasks = await db.collection('tasks').find({ project: projectId }).toArray();

    return tasks.map(task => ({
        id: task._id.toHexString(),
        project: task.project,
        title: task.title,
        description: task.description,
        status: task.status,
        deadline: task.deadline,
        editor: task.editor,
        updated: task.updated
    }));
}

/**
 * Deletes tasks by project ID.
 * @param projectId - The ID of the project whose tasks are to be deleted.
 * @returns A promise that resolves when the tasks and associated comments and notifications are deleted.
 */
async function deleteTasks(projectId: string): Promise<void> {
    const tasks = await db.collection('tasks').find({ project: new ObjectId(projectId) }).toArray();

    const taskIds = tasks.map(task => task._id);

    await db.collection('comments').deleteMany({ task: { $in: taskIds } });
    console.log(`Deleted comments for ${taskIds.length} tasks associated with project ${projectId}`);

    await db.collection('notifications').deleteMany({ task: { $in: taskIds } });
    console.log(`Deleted notifications for ${taskIds.length} tasks associated with project ${projectId}`);

    const taskDeleteResult = await db.collection('tasks').deleteMany({ _id: { $in: taskIds } });
    console.log(`Deleted ${taskDeleteResult.deletedCount} tasks associated with project ${projectId}`);
}

/**
 * Gets a task by its ID.
 * @param taskId - The ID of the task to search for.
 * @returns A promise that resolves to a TaskDto object or null if the task is not found.
 */
async function getTask(taskId: string): Promise<TaskDto | null> {
    const task = await db.collection('tasks').findOne({ _id: new ObjectId(taskId) });

    return task ? {
        id: task._id.toHexString(),
        project: task.project,
        title: task.title,
        description: task.description,
        status: task.status,
        deadline: task.deadline,
        editor: task.editor,
        updated: task.updated
    } : null;
}

/**
 * Edits an existing task.
 * @param id - The ID of the task to edit.
 * @param project - The ID of the project the task belongs to.
 * @param title - The new title of the task.
 * @param description - The new description of the task.
 * @param status - The new status of the task.
 * @param deadline - The new deadline of the task.
 * @param editor - The username of the task editor.
 * @returns A promise that resolves to a TaskDto object or null if the update fails.
 */
async function editTask(id: string, project: string, title: string, description: string, status: string, deadline: string, editor: string): Promise<TaskDto| null> {
    const updatedTask = await db.collection('tasks').findOneAndUpdate(
        { _id: new ObjectId(id) },
        { $set: { project, title, description, status, deadline, editor, updated: new Date().getTime().toString() } },
        { returnDocument: 'after' }
    );

    if (!updatedTask.value) {
        console.log("Task not found or update failed");
        return null;
    }

    const taskDto: TaskDto = {
        id: updatedTask.value._id.toHexString(),
        project: updatedTask.value.project,
        title: updatedTask.value.title,
        description: updatedTask.value.description,
        status: updatedTask.value.status,
        deadline: updatedTask.value.deadline,
        editor: updatedTask.value.editor,
        updated: updatedTask.value.updated
    };

    return taskDto;
}

/**
 * Deletes a task by its ID.
 * @param taskId - The ID of the task to delete.
 * @returns A promise that resolves to an array of TaskDto objects or null.
 */
async function deleteTask(taskId: string): Promise<TaskDto[] | null> {
    const task = await db.collection('tasks').findOne({ _id: new ObjectId(taskId) });

    if (!task) {
        console.log("Task not found");
        return [];
    }

    await db.collection('comments').deleteMany({ task: new ObjectId(taskId) });
    console.log(`Deleted comments for task ${taskId}`);

    await db.collection('notifications').deleteMany({ task: new ObjectId(taskId) });
    console.log(`Deleted notifications for task ${taskId}`);

    await db.collection('tasks').deleteOne({ _id: new ObjectId(taskId) });

    return getTasks(task.project);
}

/**
 * Gets the member icons for a given project.
 * @param projectId - The ID of the project.
 * @returns A promise that resolves to an array of member icons (picture URLs).
 */
async function getMemberIcons(projectId: string): Promise<string[]> {
    const project = await db.collection('projects').findOne({ _id: new ObjectId(projectId) });

    if(!project) {
        console.log("Project not found");
        return [];
    }

    const memberUsernames = project.members;
    const memberIcons: string[] = [];

    for(const username of memberUsernames) {
        const user = await db.collection('users').findOne({ name: username });
        if(user) {
            memberIcons.push(user.picture);
        }
    }

    return memberIcons;
}

/**
 * Gets all tasks for a given username.
 * @param username - The username to search for.
 * @returns A promise that resolves to an array of TaskDto objects.
 */
async function getAllTask(username: string): Promise<TaskDto[]> {
    // Keressük meg az összes projektet, amelyeknek a members mezőjében szerepel a username
    const projects = await db.collection('projects').find({members: username}).toArray();
    const projectIds = projects.map(project => project._id.toHexString());

    // Keressük meg az összes taskot, amelyeknek a project mezője ezek közé a projektek közé tartozik
    const tasks = await db.collection('tasks').find({ project: { $in: projectIds } }).toArray();

    return tasks.map(task => ({
        id: task._id.toHexString(),
        project: task.project,
        title: task.title,
        description: task.description,
        status: task.status,
        deadline: task.deadline,
        editor: task.editor,
        updated: task.updated
    }));
}

/**
 * Gets the comment count for a given username.
 * @param username - The username to search for.
 * @returns A promise that resolves to the number of comments authored by the username.
 */
async function getCommentCount(username: string): Promise<number> {
    return await db.collection('comments').countDocuments({ user: username });
}


//DTO-k

/**
 * Represents a user.
 */
export type UserDto = {
    id: string;
    name: string;
    email: string;
    password: string;
    picture: string; // elérési út opciói
    theme: string;
    created: string;
};

/**
 * Represents a project.
 */
export type ProjectDto = {
    id: string;
    name: string;
    description: string;
    creator: string;
    created: string;
    members: string[];
};

/**
 * Represents a task.
 */
export type TaskDto = {
    id: string;
    project: string;
    title: string;
    description: string;
    status: string;
    deadline: string;
    editor: string;
    updated: string;
};

/**
 * Represents a comment.
 */
export type CommentDto = {
    id: string;
    task: string;
    user: string;
    content: string;
    created: string;
};

/**
 * Represents a notification.
 */
export type NotificationDto = {
    id: string;
    user: string;
    task: string;
    message: string;
    type: string;
    date: string;
};

// export type OutgoingPacket =
//     { type: "login", username: string, password: string } |
//     { type: "register", username: string, email: string, password: string } |
//     { type: "edit_user", id: string, name: string, email: string, picture: string, theme: string } |
//     { type: "create_project", name: string, description: string, creator: string} |
//     { type: "open_project", id: string } |
//     { type: "delete_project", id: string, member: string } |
//     { type: "quit_project", p_id: string, m_id: string } |
//     { type: "create_task", project: string, title: string, description: string, status: string, deadline: string, editor: string} |
//     { type: "open_task", id: string } |
//     { type: "edit_task", id: string, project: string, title: string, description: string, status: string, deadline: string, editor: string} |
//     { type: "delete_task", id: string } |
//     { type: "add_member", project: string, user: string } |
//     { type: "create_comment", task: string, user: string, content: string } |
//     { type: "create_notification", user: string, task: string, message: string, notification_type: string, date: string } |
//     { type: "delete_notification", id: string } |
//     { type: "get_statistics", user: string };

//     export type IncomingPacket =
//     { type: "error", message: string } |
//     { type: "user", user: UserDto } |    // login register és edit_user után
//     { type: "projects", projects: ProjectDto[] } |   // login create_project és delete_project után
//     { type: "project", project: ProjectDto } |    // add_member után
//     { type: "task", task: TaskDto } |    // open_task után
//     { type: "tasks", tasks: TaskDto[] } |    // open_project create_task edit_task és delete_task után
//     { type: "members", members: string[] } |   // open_project és add_member után
//     { type: "comments", comments: CommentDto[] } |   // open_task és create_comment után
//     { type: "notifications", notifications: NotificationDto[] }| // create_notification és delete_notification után
//     { type: "statistics", allTask: TaskDto[], commentCount: number};   // get_statistics után