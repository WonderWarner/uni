import './NotificationToast.less';

/**
 * Props for the NotificationToast component.
 */
interface NotificationToastProps {
    message: string;
    onClose: () => void;
}

/**
 * Renders a notification toast with a close button.
 * @param message - The message to display in the toast.
 * @param onClose - Function to call when the close button is clicked.
 */
export function NotificationToast({ message, onClose }: Readonly<NotificationToastProps>) {
    return (
        <div className="notification-toast">
            <p>{message}</p>
            <button onClick={onClose}>Close</button>
        </div>
    );
}
