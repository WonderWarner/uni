import { useEffect, useState } from 'preact/hooks';
import { taskService } from '../TaskService';
import { PieChart } from './PieChart'; // Importáljuk a PieChart komponenst
import { IconButton } from '../common/IconButton';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import './StatisticsScreen.less';
import { BarChart } from './BarChart';

/**
 * Renders the statistics screen.
 */
export function StatisticsScreen() {
  const [projects, setProjects] = useState<number>(taskService.storage?.projects?.length);
  const [tasks, setTasks] = useState<number>(taskService.storage?.allTasks?.length);
  const [comments, setComments] = useState<number>(taskService.storage?.commentCount);
  const [taskStatusData, setTaskStatusData] = useState<{ labels: string[], data: number[], colors: string[] } | null>(null);

  // Adatok betöltése a taskService-ből
  const fetchStatistics = () => {

    // Felhasználó task státuszai
    const statusCounts = {
      New: 0,
      'In Progress': 0,
      Done: 0,
      Reviewed: 0
    };

    taskService.storage.allTasks.forEach(task => {
      statusCounts[task.status]++;
    });

    const labels = ['New', 'In Progress', 'Done', 'Reviewed'];
    const data = [statusCounts.New, statusCounts['In Progress'], statusCounts.Done, statusCounts.Reviewed];
    const colors = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'];

    // Állapotok statisztikai adatainak frissítése
    setTaskStatusData({
      labels,
      data,
      colors
    });

    // Egyéb statisztikai adatok frissítése
    setProjects(taskService.storage.projects.length);
    setTasks(taskService.storage.allTasks.length);
    setComments(taskService.storage.commentCount);
  };

  // Az adatok betöltése a komponens betöltésekor
  useEffect(() => {
    fetchStatistics();
  }, []);

  // Task határidők statisztikáját számolja
  const taskDeadlineStats = () => {
    const now = new Date();
    let nearDeadline = 0;
    let upcomingDeadline = 1;
    let farDeadline = 2;

    taskService.storage.allTasks.forEach(task => {
      const deadline = new Date(task.deadline);
      const diffTime = deadline.getTime() - now.getTime();
      const diffDays = diffTime / (1000 * 3600 * 24); // Átváltás napokba

      if (diffDays <= 3) {
        nearDeadline++;
      } else if (diffDays <= 30) {
        upcomingDeadline++;
      } else {
        farDeadline++;
      }
    });

    return [nearDeadline, upcomingDeadline, farDeadline];
  };

  const deadlineData = taskDeadlineStats();
  const deadlineLabels = ['Near Deadline (0-3 days)', 'Upcoming Deadline (4-30 days)', 'Far Deadline (>30 days)'];
  const deadlineColors = ['#FF6347', '#FFD700', '#90EE90'];
  const deadlineTitle = 'Task Deadline Distribution';

  return (
    <div>
      <div class="back-button">
        <IconButton
          icon={faArrowLeft}
          label="Back"
          onClick={() => window.history.back()}
        />
      </div>
      <div class="statistics-screen">
        <h1 class="title">Statistics</h1>

        <div class="statistics-data">
          <div class="stat-item">
            <strong>Total Projects:</strong> {projects}
          </div>
          <div class="stat-item">
            <strong>Total Tasks:</strong> {tasks}
          </div>
          <div class="stat-item">
            <strong>Total Comments:</strong> {comments}
          </div>
        </div>

        <div class="charts-container">
          {tasks!==0 && taskStatusData && (
            <div class="pie-chart-container">
              <h3>Task Status Distribution</h3>
              <PieChart labels={taskStatusData.labels} data={taskStatusData.data} colors={taskStatusData.colors} />
            </div>
          )}

          {tasks!==0 && (
            <div class="bar-chart-container">
              <BarChart
                title={deadlineTitle}
                labels={deadlineLabels}
                data={deadlineLabels.map((label, index) => ({ value: deadlineData[index], label }))}
                backgroundColors={deadlineColors}
              />
            </div>
          )}

        </div>
      </div>
    </div>
  );
}
