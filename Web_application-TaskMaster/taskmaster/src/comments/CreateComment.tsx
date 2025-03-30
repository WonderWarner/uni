import { useState } from 'preact/hooks';
import { OutgoingPacket, taskService } from '../TaskService';
import './CreateComment.less';

/**
 * Props for the CreateComment component.
 */
interface CreateCommentProps {
    onCancel: () => void;
    onCreate: () => void;
}

/**
 * Renders a modal for creating a new comment.
 * @param onCancel - Function to call when the cancel button is clicked.
 * @param onCreate - Function to call when the comment is successfully created.
 */
export function CreateComment({onCancel, onCreate }: Readonly<CreateCommentProps>) {
    const [commentContent, setCommentContent] = useState('');

    /**
     * Handles the save action for the new comment.
     */
    const handleSave = () => {
        if (commentContent.trim()) {
            const packet: OutgoingPacket = {
                type: 'create_comment',
                task: taskService.storage.task?.id || '',
                user: taskService.storage.user?.name || '',
                content: commentContent
            };

            try {
                taskService.send(packet);
                onCreate();
                console.log("Comment created:", commentContent);
            } catch (error) {
                console.error("Error while creating:", error);
                alert("An error occured while creating the comment!");
            }
        }
    };

    return (
        <div class="create-comment-modal">
            <h3>Add New Comment</h3>
            <textarea
                value={commentContent}
                onInput={(e) => setCommentContent(e.currentTarget.value)}
                placeholder="Enter your comment here..."
                required
            />
            <div class="comment-actions">
                <button onClick={onCancel}>Cancel</button>
                <button onClick={handleSave}>Save</button>
            </div>
        </div>
    );
}
