import { route } from 'preact-router';
import { ImageButton } from '../common/ImageButton';
import { OutgoingPacket, taskService } from '../TaskService';
import './OverviewScreen.less';

/**
 * Renders the overview screen with the main dashboard as navigation buttons.
 */
export function OverviewScreen() {
  /**
   * Navigates to the specified path.
   * @param path - The path to navigate to.
   */
  const navigateTo = (path: string) => {
    route(path);
  };

  return (
    <div class="overview-screen">
      <h1>Dashboard</h1>

      <div class="button-container">
        <ImageButton
          imageSrc="/res/settings.png"
          label="Settings"
          onClick={() => navigateTo('/settings')}
        />
        <ImageButton
          imageSrc="/res/notifications.png"
          label="Notifications"
          onClick={() => {
            navigateTo('/notifications');
            const packet: OutgoingPacket = {
              type: 'get_statistics',
              user: taskService.storage?.user?.name,
            };
            taskService.send(packet);
          }}
        />
        <ImageButton
          imageSrc="/res/projects.png"
          label="Projects"
          onClick={() => navigateTo('/projects')}
        />
        <ImageButton
          imageSrc="/res/statistics.png"
          label="Statistics"
          onClick={() => {
            navigateTo('/statistics');
            const packet: OutgoingPacket = {
              type: 'get_statistics',
              user: taskService.storage?.user?.name,
            };
            taskService.send(packet);
          }}
        />
      </div>
    </div>
  );
}
