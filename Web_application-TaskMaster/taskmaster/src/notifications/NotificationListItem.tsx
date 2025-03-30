import { NotificationDto } from '../TaskService';
import './NotificationListItem.less';

/**
 * Props for the NotificationListItem component.
 */
interface NotificationListItemProps {
    notification: NotificationDto;
    onDelete: (id: string) => void;
}

/**
 * Renders a single notification item.
 * @param notification - The notification data to display.
 * @param onDelete - Function to call when the delete button is clicked.
 */
export function NotificationListItem({ notification, onDelete }: Readonly<NotificationListItemProps>) {
    return (
        <div class="notification-item">
            <div class="notification-content">
                <p class="notification-message">{notification.message}</p>
                <p class="notification-type-date">{notification.type} - {new Date(notification.date).toLocaleString()}</p>
            </div>
            <button class="delete-button" onClick={() => onDelete(notification.id)}>
                Delete
            </button>
        </div>
    );
}
