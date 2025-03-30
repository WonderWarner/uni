import { useEffect, useState } from 'preact/hooks';
import { route } from 'preact-router';
import { taskService, TaskDto, CommentDto, OutgoingPacket } from '../TaskService';
import { CommentListItem } from './CommentListItem';
import { CreateComment } from './CreateComment';
import { IconButton } from '../common/IconButton';
import { faArrowLeft, faAdd } from '@fortawesome/free-solid-svg-icons';
import { useCompletionEffect} from './FinishedTask';
import './CommentsScreen.less';

/**
 * Renders the comments screen for a specific task.
 */
export function CommentsScreen() {
    const playEffect = useCompletionEffect(); // Hook meghívása a komponenst elején
    const [isAnimationVisible, setAnimationVisible] = useState(false);

    const [task, setTask] = useState<TaskDto | null>(null);
    const [comments, setComments] = useState<CommentDto[]>([]);
    const [actStatus, setStatus] = useState<string>('');
    const [isCreateCommentOpen, setIsCreateCommentOpen] = useState(false);

    /**
     * Fetches the task and comments from the task service and updates the state.
     */
    const fetchTask = () => {
        setTask(taskService.storage?.task);
        setComments([...taskService.storage.comments]);
        setStatus(taskService.storage?.task?.status);
    };

    /**
     * Handles the creation of a new comment.
     */
    const handleCreateComment = () => {
        setIsCreateCommentOpen(false); // Bezárja a felugró ablakot
        fetchTask(); // Frissíti az állapotot
    }

    /**
     * Updates the task status and sends the update to the task service.
     * @param status - The new status of the task.
     */
    const handleUpdateTaskStatus = (status: string) => {
        setStatus(status);
        const packet: OutgoingPacket = {
            type: "edit_task",
            id: task?.id,
            project: task?.project,
            title: task?.title,
            description: task?.description,
            status: status,
            deadline: task?.deadline,
            editor: taskService.storage.user.name
        };
        taskService.send(packet);
        fetchTask();
        if(status === 'Done') {
            setAnimationVisible(true);
            setTimeout(() => setAnimationVisible(false), 2000);
            playEffect(); // Hook által visszaadott függvény meghívása
        }
    };

    // Listener hozzáadása a komponens betöltésekor
    useEffect(() => {
        taskService.addListener(fetchTask); // Hallgató eseményekre
        fetchTask(); // Azonnali frissítés
        return () => taskService.removeListener(fetchTask); // Tisztítás
    }, []);

    const formattedDate = new Date(Number(task?.updated)).toLocaleDateString();

    return (
        <div class="comments-screen">
            <div id="completion-icon" className={isAnimationVisible ? "animate" : "hidden"}>✔️</div>
            <div class="back-button">
                <IconButton
                    icon={faArrowLeft}
                    label="Back"
                    onClick={() => route(`/projects/${task?.project}`)} // Vissza a TasksScreen-re
                />
            </div>
            <h1 class="title">{task?.title} - Comments</h1>

            <div class="task-summary">
                <div class="task-details">
                    <p><strong>Description:</strong> {task?.description}</p>
                    <p><strong>Deadline:</strong> {task?.deadline}</p>
                    <p><strong>Last modified by:</strong> {task?.editor}</p>
                    <p><strong>Last modified at:</strong> {formattedDate}</p>
                </div>
                <div class="task-status">
                    <h3>Status:</h3>
                    <div class="status-options">
                        {['New', 'In Progress', 'Done', 'Reviewed'].map(status => (
                            <button
                                key={status}
                                class={`status-button ${actStatus === status ? 'active' : ''}`}
                                onClick={() => handleUpdateTaskStatus(status)}
                            >
                                {status}
                            </button>
                        ))}
                    </div>
                </div>
            </div>

            <div class="create-button">
                <IconButton
                    icon={faAdd}
                    label="New Comment"
                    onClick={() => setIsCreateCommentOpen(true)}
                />
            </div>

            <div class="comment-list">
                {comments.length > 0 ? (
                    comments.map(comment => (
                        <CommentListItem key={comment.id} comment={comment} />
                    ))
                ) : (
                    <p class="no-comments">No comments written yet.</p>
                )}
            </div>
            {isCreateCommentOpen && (
                <CreateComment
                    onCancel={() => setIsCreateCommentOpen(false)}
                    onCreate={handleCreateComment}
                />
            )}
        </div>
    );
}