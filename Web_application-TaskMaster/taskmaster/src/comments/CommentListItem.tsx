import { CommentDto } from '../TaskService';
import './CommentListItem.less';

/**
 * Props for the CommentListItem component.
 */
interface CommentListItemProps {
    comment: CommentDto;
}

/**
 * Renders a single comment item.
 * @param comment - The comment data to display.
 */
export function CommentListItem({ comment }: Readonly<CommentListItemProps>) {
    const {user, content, created } = comment;
    return (
        <div class="comment-list-item">
            <div class="comment-header">
                <span class="comment-author">{user}</span>
                <span class="comment-date">{new Date(Number(created)).toLocaleDateString()}</span>
            </div>
            <div class="comment-content">
                <p>{content}</p>
            </div>
        </div>
    );
}
