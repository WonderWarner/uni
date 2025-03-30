import { ProjectDto, OutgoingPacket, taskService } from '../TaskService';
import { useState } from 'preact/hooks';
import { route } from 'preact-router';
import './ProjectListItem.less';

/**
 * Renders a single project item.
 * @param project - The project data to display.
 */
export function ProjectListItem({ project }: Readonly<{ project: ProjectDto }>) {
    const {id, name, creator, created, members } = project;
    const [hovered, setHovered] = useState(false);

    /**
     * Handles the click event to open the project.
     */
    const handleOpenClick = () => {
        const packet: OutgoingPacket = {
            type: 'open_project',
            id: id
        };
        taskService.send(packet);
        route(`/projects/${project.id}`);
    };

    const formattedDate = new Date(Number(created)).toLocaleDateString();
    const displayMembers = members.slice(1, 3).join(', ');
    const additionalCount = members.length > 3 ? `and ${members.length - 3} more` : '';

    return (
        <div class={`project-card ${hovered ? 'hovered' : ''}`}
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
        >
            <div class="project-header">
                <h2>{name}</h2>
                {hovered && (
                    <button class="open-button" onClick={handleOpenClick}>Open</button>
                )}
            </div>

            <div class="project-details">
                <p>
                    <strong>Created by:</strong> {creator}
                </p>
                <p>
                    <strong>Created at:</strong> {formattedDate}
                </p>
                <p>
                    <strong>Members:</strong> {displayMembers || "No additional members"} {additionalCount && <span class="more-members">{additionalCount}</span>}
                </p>
            </div>
        </div>
    );
}
