import { useState } from 'preact/hooks';
import { DatePicker } from '../common/DatePicker';
import { OutgoingPacket, taskService } from '../TaskService';
import './CreateTask.less';
import { LabeledInput } from '../common/LabeledInput';

/**
 * Props for the CreateTask component.
 */
interface CreateTaskProps {
    onCancel: () => void;
    onCreate: () => void;
}

/**
 * Renders a modal for creating a new task.
 * @param onCancel - Function to call when the cancel button is clicked.
 * @param onCreate - Function to call when the task is created.
 */
export function CreateTask({ onCancel, onCreate }: Readonly<CreateTaskProps>) {
    const [taskTitle, setTaskTitle] = useState('');
    const [taskDescription, setTaskDescription] = useState('');
    const [taskDeadline, setTaskDeadline] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    /**
     * Handles the creation of a new task.
     */
    const handleCreateTask = async () => {
        if (taskTitle.trim() === '') {
            alert("Task name is required!");
            return;
        }
        if (!taskDeadline) {
            alert("Task date is required!");
            return;
        }
        setIsSubmitting(true);

        const packet: OutgoingPacket = {
            type: 'create_task',
            project: taskService.storage.project?.id || '',
            title: taskTitle,
            description: taskDescription,
            status: 'New',
            deadline: taskDeadline,
            editor: taskService.storage.user?.name || ''
        };

        try {
            taskService.send(packet);
            onCreate();
            console.log("Task created:", taskTitle);
        } catch (error) {
            console.error("Error while creating:", error);
            alert("An error occured while creating the task!");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div class="create-task-overlay">
            <div class="create-task-modal">
                <h2>Create New Task</h2>
                <div class="form-group">
                    <LabeledInput
                        label="Task Title"
                        type="text"
                        placeholder="Add task title"
                        value={taskTitle}
                        onChange={setTaskTitle}
                        autofocus
                    />
                </div>
                <div class="form-group">
                    <LabeledInput
                        label="Description (optional)"
                        type="text"
                        placeholder="What is the goal of the task?"
                        value={taskDescription}
                        onChange={setTaskDescription}
                    />
                </div>
                <div class="form-group">
                    <h5>Due Date</h5>
                    <DatePicker value={taskDeadline} onChange={setTaskDeadline} />
                </div>
                <div class="modal-footer">
                    <button class="cancel-btn" onClick={onCancel} disabled ={isSubmitting}>Cancel</button>
                    <button class="save-btn" onClick={handleCreateTask} disabled={isSubmitting || !taskDeadline || taskTitle.trim() === ''}>Save</button>
                </div>
            </div>
        </div>
    );
}
