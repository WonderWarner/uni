import { useState } from 'preact/hooks';
import { LabeledInput } from '../common/LabeledInput';
import { taskService, OutgoingPacket } from '../TaskService';
import './CreateProject.less';

/**
 * Renders a dialog for creating a new project.
 * @param onClose - Function to call when the cancel button is clicked.
 * @param onCreate - Function to call when the project is successfully created.
 */
export function CreateProject({ onClose, onCreate }: Readonly<{ onClose: () => void, onCreate: () =>void }>) {
    const [projectName, setProjectName] = useState('');
    const [description, setDescription] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    /**
     * Handles the creation of a new project.
     */
    const handleCreateProject = async () => {
        if (!projectName) {
            alert("Project name is required!");
            return;
        }
        setIsSubmitting(true);

        const packet: OutgoingPacket = {
            type: 'create_project',
            name: projectName,
            description: description,
            creator: taskService.storage.user?.name || ''
        };

        try {
            taskService.send(packet);
            onCreate();
            console.log("Project created:", projectName);
        } catch (error) {
            console.error("Error while creating:", error);
            alert("An error occured while creating the project!");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div class="create-project-dialog">
            <div class="card">
                <h2>Create Project</h2>
                <LabeledInput
                    label="Project Name"
                    type="text"
                    placeholder="Enter project name"
                    value={projectName}
                    onChange={setProjectName}
                    autofocus
                />
                <LabeledInput
                    label="Description (optional)"
                    type="text"
                    placeholder="What is this project about?"
                    value={description}
                    onChange={setDescription}
                />
                <div class="actions">
                    <button onClick={onClose} disabled={isSubmitting}>
                        Cancel
                    </button>
                    <button onClick={handleCreateProject} disabled={isSubmitting || !projectName}>
                        Save
                    </button>
                </div>
            </div>
        </div>
    );
    
}