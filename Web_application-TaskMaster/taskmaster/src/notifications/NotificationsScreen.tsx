import { useState, useEffect } from 'preact/hooks';
import { NotificationListItem } from './NotificationListItem';
import { taskService, NotificationDto, OutgoingPacket } from '../TaskService';
import { route } from 'preact-router';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { IconButton } from '../common/IconButton';
import './NotificationsScreen.less';

/**
 * Renders the notifications screen.
 */
export function NotificationsScreen() {
    const [notifications, setNotifications] = useState<NotificationDto[]>([]);
    const [newNotification, setNewNotification] = useState({
        message: '',
        type: 'Reminder',
        date: '',
        task: ''
    });

    /**
     * Fetches existing notifications from the service and updates the state.
     */
    const fetchNotifications = () => {
        // Fetch existing notifications from the service
        setNotifications(taskService.storage.notifications);
    };

    /**
     * Handles the addition of a new notification.
     */
    const handleAddNotification = () => {
        if (!newNotification.message || !newNotification.date || !newNotification.task) {
            alert('Please fill in all required fields.');
            return;
        }
        // Create packet and send it to the server
        const packet: OutgoingPacket = {
            type: 'create_notification',
            user: taskService.storage.user.name,
            task: newNotification.task,
            message: newNotification.message,
            notification_type: newNotification.type,
            date: newNotification.date
        };
        taskService.send(packet);
        fetchNotifications();
    };

    /**
     * Handles the deletion of a notification.
     * @param id - The ID of the notification to delete.
     */
    const handleDeleteNotification = (id: string) => {
        const packet: OutgoingPacket = {
            type: 'delete_notification',
            id: id
        };
        taskService.send(packet);
        fetchNotifications();
    };

    useEffect(() => {
        taskService.addListener(fetchNotifications);
        fetchNotifications();
        return () => taskService.removeListener(fetchNotifications);
    }, []);

    return (
        <div class="notification-screen">
            <div class="notification-header">
                <IconButton icon={faArrowLeft} label="Back" onClick={() => route('/')} />
                <h1>Notifications</h1>
            </div>
            <div class="notification-list">
                {notifications.map((notification) => (
                    <NotificationListItem
                        key={notification.id}
                        notification={notification}
                        onDelete={handleDeleteNotification}
                    />
                ))}
            </div>
            <div class="new-notification-form">
                <h2>Schedule New Notification</h2>
                <input
                    type="text"
                    placeholder="Message"
                    value={newNotification.message}
                    onInput={(e) => setNewNotification({ ...newNotification, message: e.currentTarget.value })}
                />
                <select
                    value={newNotification.type}
                    onChange={(e) => setNewNotification({ ...newNotification, type: e.currentTarget.value })}
                >
                    <option value="Reminder">Reminder</option>
                    <option value="Alert">Alert</option>
                </select>
                <input
                    type="datetime-local"
                    value={newNotification.date}
                    onChange={(e) => setNewNotification({ ...newNotification, date: e.currentTarget.value })}
                />
                <select
                    value={newNotification.task}
                    onChange={(e) => setNewNotification({ ...newNotification, task: e.currentTarget.value })}
                >
                    <option value="">Select Task</option>
                    {taskService.storage.allTasks.map((task) => (
                        <option key={task.id} value={task.id}>
                            {task.title}
                        </option>
                    ))}
                </select>
                <button onClick={handleAddNotification}>Save</button>
            </div>
        </div>
    );
}
