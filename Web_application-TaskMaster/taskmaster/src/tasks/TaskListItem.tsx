import { useState } from 'preact/hooks';
import { route } from 'preact-router';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircle, faCheckCircle, faHourglassHalf, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import './TaskListItem.less';
import { TaskDto, taskService, OutgoingPacket } from '../TaskService';

/**
 * Renders a single task item.
 * @param task - The task data to display.
 */
export function TaskListItem({ task }: Readonly<{task: TaskDto}>) {
    const {id, title, status, deadline } = task;
    const [hovered, setHovered] = useState(false);

    /**
     * Handles the click event to view the task.
     */
    const handleViewClick = () => {
        const packet: OutgoingPacket = {
            type: 'open_task',
            id: id
        };
        taskService.send(packet);
        route(`/projects/${taskService.storage.project.id}/${id}`);
    };

    // Dátum konvertálás a színezéshez
    const deadlineObj = new Date(deadline);
    const timeDifference = deadlineObj.getTime() - Date.now();
    let dueDateColor = '#FFA500'; // Alapértelmezett: sárga

    if (timeDifference <= 3 * 24 * 60 * 60 * 1000) { // Ha 3 napon belüli a határidő
        dueDateColor = '#FF0000'; // Piros
    } else if (timeDifference > 30 * 24 * 60 * 60 * 1000) { // Ha több mint 1 hónap
        dueDateColor = '#4CAF50'; // Zöld
    }

    // Ikon státusz szerint
    let statusIcon;
    switch (status) {
        case 'New':
            statusIcon = faCircle;
            break;
        case 'In Progress':
            statusIcon = faHourglassHalf;
            break;
        case 'Done':
            statusIcon = faCheckCircle;
            break;
        case 'Reviewed':
            statusIcon = faTimesCircle;
            break;
    }

    return (
        <div 
            class="task-list-item" 
            onMouseEnter={() => setHovered(true)} 
            onMouseLeave={() => setHovered(false)}
        >
            <div class="task-header">
                <span class="task-title">{title}</span>
                {hovered && (
                    <button class="view-button" onClick={handleViewClick}>View</button>
                )}
            </div>

            <div class="task-footer">
                <FontAwesomeIcon icon={statusIcon} class="status-icon" />
                <span class="deadline" style={{ color: dueDateColor }}>
                    Deadline: {deadline}
                </span>
            </div>
        </div>
    );
}
