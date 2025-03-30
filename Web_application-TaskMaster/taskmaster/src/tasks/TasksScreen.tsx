import { useEffect, useState } from 'preact/hooks';
import { route } from 'preact-router';
import { taskService, TaskDto, ProjectDto, OutgoingPacket } from '../TaskService';
import { ProfileIcon } from '../common/ProfileIcon';
import { TaskListItem } from './TaskListItem';
import { CreateTask } from './CreateTask';
import { IconButton } from '../common/IconButton';
import { LabeledInput } from '../common/LabeledInput';
import { faArrowLeft, faAdd } from '@fortawesome/free-solid-svg-icons';
import './TasksScreen.less';

/**
 * Renders the tasks screen for a specific project.
 */
export function TasksScreen() {
    const [project, setProject] = useState<ProjectDto | null>(null);
    const [tasks, setTasks] = useState<TaskDto[]>([]);
    const [memberIcons, setMemberIcons] = useState<string[]>([]);
    const [isCreateTaskOpen, setIsCreateTaskOpen] = useState(false);
    const [newMember, setNewMember] = useState('');
    const [addMemberDisabled, setAddMemberDisabled] = useState(true);

    /**
     * Fetches the project and tasks from the task service and updates the state.
     */
    const fetchProject = () => {
        setProject(taskService.storage.project);
        setTasks([...taskService.storage.tasks]);
        setMemberIcons([...taskService.storage.memberIcons]);
    };

    /**
     * Handles the creation of a new task.
     */
    const handleCreateTask = () => {
        setIsCreateTaskOpen(false); // Bezárja a felugró ablakot
        fetchProject(); // Frissíti az állapotot
    }

    /**
     * Handles the addition of a new member to the project.
     */
    const handleAddMember = () => {
        if (project && newMember.trim() && !project.members.includes(newMember)) {
            const packet: OutgoingPacket = {
                type: 'add_member',
                project: project.id,
                user: newMember
            };
            try {
                taskService.send(packet);
                console.log("Adding member:", newMember);
            } catch (error) {
                console.error("Error while adding:", error);
                alert("An error occured while adding member to the project!");
            }
            setNewMember('');
            setAddMemberDisabled(true);
            fetchProject();
        }
    }

    // Listener hozzáadása a komponens betöltésekor
    useEffect(() => {
        taskService.addListener(fetchProject); // Hallgató eseményekre
        fetchProject(); // Azonnali frissítés
        return () => taskService.removeListener(fetchProject); // Tisztítás
    }, []);

    useEffect(() => {
        setAddMemberDisabled(newMember.trim() === '');
    }, [newMember]);

    return (
        <div class="tasks-screen">
            <div class="add-member-container">
                <LabeledInput
                    label="New member"
                    type="text"
                    value={newMember}
                    onChange={setNewMember}
                    placeholder="Enter member name"
                />
                <IconButton
                    icon={faAdd}
                    label="Add"
                    onClick={handleAddMember}
                    disabled={addMemberDisabled}
                />
            </div>
            <div class="back-button">
                <IconButton
                    icon={faArrowLeft}
                    label="Back"
                    onClick={() => route('/projects')} // Vissza a ProjectsScreen-re
                />
            </div>
            <h1 class="title">{project?.name} - Tasks</h1>

            <div class="project-summary">
                <p><strong>Description:</strong> {project?.description}</p>
                <p><strong>Creator:</strong> {project?.creator}</p>
                <div class="members">
                    <h3>Members:</h3>
                    <div class="profile-icon-container">
                        {project?.members?.map((member, index) => (
                            <ProfileIcon
                                key={member}
                                name={member}
                                icon={memberIcons[index]}
                            />
                        )) || <p>No members found</p>}
                    </div>
                </div>
            </div>

            <div class="create-button">
                <IconButton
                    icon={faAdd}
                    label="New Task"
                    onClick={() => setIsCreateTaskOpen(true)}
                />
            </div>

            <div class="task-list">
                {tasks.length > 0 ? (
                    tasks.map(task => (
                        <TaskListItem key={task.id} task={task} />
                    ))
                ) : (
                    <p class="no-tasks">No tasks found</p>
                )}
            </div>
            {isCreateTaskOpen && (
                <CreateTask
                    onCancel={() => setIsCreateTaskOpen(false)}
                    onCreate={handleCreateTask}
                />
            )}
        </div>
    );
}
