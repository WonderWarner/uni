import { useEffect, useRef } from 'preact/hooks';
import { taskService, NotificationDto, OutgoingPacket } from '../TaskService';


/**
 * Props for the useNotificationManager hook.
 */
interface NotificationManagerProps {
    onNotification: (notification: NotificationDto) => void;
}

/**
 * Custom hook to manage notifications.
 * @param onNotification - Function to call when a notification is received.
 */
export function useNotificationManager({ onNotification }: NotificationManagerProps) {
    const intervalRef = useRef<number | null>(null);

    /**
     * Checks for notifications and displays them if they are due.
     */
    const checkNotifications = () => {
        const now = new Date();
        const notifications = taskService.storage.notifications || [];
        if (!notifications.length) return;

        notifications.forEach((notification: NotificationDto) => {
            const notificationDate = new Date(notification.date);
            const notificationsToDelete: string[] = [];
            if (notificationDate <= now) {
                displayNotification(notification);
                notificationsToDelete.push(notification.id);
            }
            notificationsToDelete.forEach((id) => {
                const packet: OutgoingPacket = {
                    type: 'delete_notification',
                    id: id
                };
                taskService.send(packet);
            });
            taskService.storage.notifications = notifications.filter((n) => !notificationsToDelete.includes(n.id));
        });
        const packet: OutgoingPacket = {
            type: 'get_statistics',
            user: taskService.storage?.user?.name
        };
        taskService.send(packet);
    };

    /**
     * Displays a notification with an audio alert.
     * @param notification - The notification to display.
     */
    const displayNotification = (notification: NotificationDto) => {
        // Audio figyelmeztetés
        let audio = new Audio('/res/alert.wav');
        if(notification.type === 'Reminder') {
            audio = new Audio('/res/reminder.wav');
        }
        audio.play();
        // Megjelenítés (alert)
        onNotification(notification);
    };

    useEffect(() => {
        intervalRef.current = window.setInterval(checkNotifications, 10000); // 10 másodpercenként ellenőrzés
        return () => {
            if (intervalRef.current) clearInterval(intervalRef.current);
        };
    }, []);
}
