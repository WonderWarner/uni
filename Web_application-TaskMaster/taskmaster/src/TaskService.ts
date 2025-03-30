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

/**
 * Represents a packet sent to the server.
 */
export type OutgoingPacket =
    { type: "login", username: string, password: string } |
    { type: "register", username: string, email: string, password: string } |
    { type: "edit_user", id: string, name: string, email: string, picture: string, theme: string } |
    { type: "create_project", name: string, description: string, creator: string } |
    { type: "open_project", id: string } |
    { type: "delete_project", id: string } |
    { type: "quit_project", p_id: string, m_id: string } |
    { type: "create_task", project: string, title: string, description: string, status: string, deadline: string, editor: string } |
    { type: "open_task", id: string } |
    { type: "edit_task", id: string, project: string, title: string, description: string, status: string, deadline: string, editor: string } |
    { type: "delete_task", id: string } |
    { type: "add_member", project: string, user: string } |
    { type: "create_comment", task: string, user: string, content: string } |
    { type: "create_notification", user: string, task: string, message: string, notification_type: string, date: string } |
    { type: "delete_notification", id: string } |
    { type: "get_statistics", user: string };

/**
 * Represents a packet received from the server.
 */
export type IncomingPacket =
    { type: "error", message: string } |
    { type: "user", user: UserDto } |    // login register és edit_user után
    { type: "projects", projects: ProjectDto[] } |   // login create_project és delete_project után
    { type: "project", project: ProjectDto } |    // add_member után
    { type: "task", task: TaskDto } |    // open_task után
    { type: "tasks", tasks: TaskDto[] } |    // open_project create_task edit_task és delete_task után
    { type: "members", members: string[] } |   // open_project és add_member után
    { type: "comments", comments: CommentDto[] } |   // open_task és create_comment után
    { type: "notifications", notifications: NotificationDto[] } | // create_notification és delete_notification után
    { type: "statistics", allTask: TaskDto[], commentCount: number};   // get_statistics után

/**
 * Represents the storage of the task service that the client can access,
 * so through this the client can stay up to date with the server.
 */
export type Storage = {
    user: UserDto;
    projects: ProjectDto[];
    project: ProjectDto;
    tasks: TaskDto[];
    task: TaskDto;
    memberIcons: string[];
    comments: CommentDto[];
    notifications: NotificationDto[];
    allTasks: TaskDto[];
    commentCount: number;
}

/**
 * Service for managing tasks, projects, and related data.
 * Communicating with server and storing session data.
 */
class TaskService {
    storage: Storage;
    readonly #ws = new WebSocket('ws://localhost:8080');
    #listeners: (() => void)[] = [];
    constructor() {
        this.storage = {
            user: undefined,
            projects: [],
            project: undefined,
            tasks: [],
            task: undefined,
            memberIcons: [],
            comments: [],
            notifications: [],
            allTasks: [],
            commentCount: 0
        };
        this.#ws.addEventListener("message", e => {
            try {
                let p = JSON.parse(e.data) as IncomingPacket;
                console.log(p); // DEBUG
                switch (p.type) {
                    case "error":
                        alert(p.message);
                        break;
                    case "user":
                        this.storage.user = p.user;
                        break;
                    case "projects":
                        this.storage.projects = p.projects;
                        break;
                    case "project":
                        this.storage.project = p.project;
                        break;
                    case "task":
                        this.storage.task = p.task;
                        break;
                    case "tasks":
                        this.storage.tasks = p.tasks;
                        break;
                    case "comments":
                        this.storage.comments = p.comments;
                        break;
                    case "notifications":
                        this.storage.notifications = p.notifications;
                        break;
                    case "members":
                        this.storage.memberIcons = p.members;
                        break;
                    case "statistics":
                        this.storage.allTasks = p.allTask;
                        this.storage.commentCount = p.commentCount;
                        break;
                }
                for (let listener of this.#listeners) {
                    try {
                        listener();
                    } catch (error) {
                        console.error("Error in listener:", error);
                    }
                }
            } catch (error) {
                console.error("Error in message event:", error);
            }
        });
    }

    /**
     * Sends a packet to the server.
     * @param packet - The packet to send.
     */
    send(packet: OutgoingPacket) {
        this.#ws.send(JSON.stringify(packet));
    }

    /**
     * Adds a listener for storage updates.
     * @param listener - The listener function.
     */
    addListener(listener: () => void) {
        this.#listeners = [...this.#listeners, listener];
    }

    /**
     * Removes a listener for storage updates.
     * @param listener - The listener function.
     */
    removeListener(listener: () => void) {
        this.#listeners = this.#listeners.filter(x => x !== listener);
    }
}

// Egy példányát elérhezővé teszi
export const taskService = new TaskService();