import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale);

/**
 * Properties for the PieChart component.
 */
interface PieChartProps {
  labels: string[];
  data: number[];
  colors: string[];
}

/**
 * Renders a pie chart.
 * @param labels - The labels for the chart sections.
 * @param data - The data values for the chart sections.
 * @param colors - The colors for the chart sections.
 */
export const PieChart = ({ labels, data, colors }: PieChartProps) => {
  const chartData = {
    labels,
    datasets: [{
      data,
      backgroundColor: colors,
      borderColor: colors.map(color => `${color}99`), // Optional: for borders
      borderWidth: 1,
    }],
  };

  return <Pie data={chartData} />;
};
