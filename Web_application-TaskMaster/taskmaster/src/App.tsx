import './App.less';
import Router, { Route } from 'preact-router';
import { useEffect, useState } from 'preact/hooks';
import { LoginScreen } from './login/LoginScreen';
import { OverviewScreen } from './overview/OverviewScreen';
import { ProjectsScreen } from './projects/ProjectsScreen';
import { TasksScreen } from './tasks/TasksScreen';
import { CommentsScreen } from './comments/CommentsScreen';
import { StatisticsScreen } from './statistics/StatisticsScreen';
import { SettingsScreen } from './settings/SettingsScreen';
import { NotificationsScreen } from './notifications/NotificationsScreen';
import { taskService } from './TaskService';

import { useNotificationManager } from './notifications/NotificationManager';
import { NotificationToast } from './notifications/NotificationToast';

/**
 * Renders the main application component.
 */
export function App() {
    const [toastMessage, setToastMessage] = useState<string | null>(null);
    // Saját hook használata a Notification pollozáson keresztüli figyeléséhez
    useNotificationManager({
        onNotification: (notification) => {
            setToastMessage(notification.message);
            setTimeout(() => setToastMessage(null), 5000);
        },
    });

    // Login logika
    let [loggedIn, setLoggedIn] = useState(false);
    useEffect(() => {
        const listener = () => {
            if (taskService.storage.user !== undefined) {
                setLoggedIn(true);
            }
        }
        taskService.addListener(listener);
        return () => taskService.removeListener(listener);
    }, []);

    if (!loggedIn) {
        return <LoginScreen />;
    }
    
    //Router használata a különböző képernyők megjelenítéséhez
    return (
        <div>
            {toastMessage && <NotificationToast message={toastMessage} onClose={() => setToastMessage(null)}/> }
            <Router>
                <Route path="/" component={OverviewScreen} />
                <Route path="/projects" component={ProjectsScreen} />
                <Route path="/projects/:id" component={TasksScreen} />
                <Route path="/projects/:projectId/:taskId" component={CommentsScreen} />
                <Route path="/statistics" component={StatisticsScreen} />
                <Route path="/settings" component={SettingsScreen} />
                <Route path="/notifications" component={NotificationsScreen} />
            </Router>
        </div>
    );

}