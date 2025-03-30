import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import { color } from 'chart.js/helpers';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

/**
 * Properties for the BarChart component.
 */
interface BarChartProps {
    title: string, 
    labels: string[];
    data: { value: number, label: string }[];
    backgroundColors: string[];
}

/**
 * Renders a bar chart.
 * @param title - The title of the chart.
 * @param labels - The labels for the x-axis.
 * @param data - The data points to display.
 * @param backgroundColors - The background colors for the bars.
 */
export function BarChart({title, labels, data, backgroundColors }: Readonly<BarChartProps>) {
    const chartData = {
        labels: labels,
        datasets: [
            {
                label: title,
                data: data.map(d => d.value),
                backgroundColor: backgroundColors,
                borderColor: 'rgba(0, 123, 255, 0.5)',
                borderWidth: 1
            }
        ]
    };

    const options = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: title,
                font: {
                    size: 20
                },
                color: 'white',
                bold : true
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        const index = context.dataIndex;
                        return `${data[index].label}: ${context.raw}`;
                    }
                }
            }
        }
    };

    return <Bar data={chartData} options={options} style={{ width: '800px', height: '800px' }} />;
}
