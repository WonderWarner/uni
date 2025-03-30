import { useEffect, useState } from 'preact/hooks';
import { route } from 'preact-router';
import { faAdd, faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { taskService, ProjectDto } from '../TaskService';
import { ProjectListItem } from './ProjectListItem';
import { IconButton } from '../common/IconButton';
import { CreateProject } from './CreateProject';
import './ProjectsScreen.less';

/**
 * Renders the projects screen.
 */
export function ProjectsScreen() {
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [isCreateProjectOpen, setIsCreateProjectOpen] = useState(false);

    /**
     * Fetches the projects from the task service and updates the state.
     */
    const fetchProjects = () => {
        console.log('Fetching projects of user: ', taskService.storage.user);
        setProjects([...taskService.storage.projects]); // Frissít az aktuális adatokkal
    };

    /**
     * Handles the creation of a new project.
     */
    const handleCreateProject = () => {
        setIsCreateProjectOpen(false); // Bezárja a felugró ablakot
        fetchProjects(); // Frissíti az állapotot
    };

    // Listener hozzáadása a komponens betöltésekor
    useEffect(() => {
        taskService.addListener(fetchProjects); // Hallgató eseményekre
        fetchProjects(); // Azonnali frissítés
        return () => taskService.removeListener(fetchProjects); // Tisztítás
    }, []);

    return (
        <div class="projects-screen">
            <div class="back-button">
                <IconButton
                    icon={faArrowLeft}
                    label="Back"
                    onClick={() => route('/')} // Navigálás vissza az OverviewScreen-re
                />
            </div>
            <h1 class="title">Projects</h1>
            <div class="create-button">
                <IconButton
                    icon={faAdd}
                    label="New Project"
                    onClick={() => setIsCreateProjectOpen(true)}
                />
            </div>
            <div class="project-list">
                {projects.length > 0 ? (
                    projects.map(project => (
                        <ProjectListItem key={project.id} project={project} />
                    ))
                ) : (
                    <p class="no-projects">No projects found</p>
                )}
            </div>
            {isCreateProjectOpen && (
                <CreateProject
                    onClose={() => setIsCreateProjectOpen(false)}
                    onCreate={handleCreateProject}
                />
            )}
        </div>
    );
}


/*
export function ProjectsScreen() {
    const [projects, setProjects] = useState<ProjectDto[]>([]);
    const [isCreateProjectOpen, setIsCreateProjectOpen] = useState(false);

    // Frissíti az állapotot a taskService adatai alapján
    function createProject() {
        setIsCreateProjectOpen(false);
        setProjects(taskService.storage.projects);
    }

    
    useEffect(() => {
        // Fetch projects from the service
        const fetchProjects = () => {
            console.log('Fetching projects of user: ', taskService.storage.user);
            console.log(taskService.storage.projects);
            setProjects(taskService.storage.projects);
        };

        taskService.addListener(fetchProjects);
        fetchProjects(); // Initial fetch
        return () => taskService.removeListener(fetchProjects);
    }, []);

    return (
        <div class="projects-screen">
            <div class="back-button">
                <IconButton
                    icon={faArrowLeft}
                    label="Back"
                    onClick={() => route('/')} // Navigálás vissza az OverviewScreen-re
                />
            </div>
            <h1 class="title">Projects</h1>
            <div class="create-button">
                <IconButton icon={faAdd} label="New Project" onClick={() => setIsCreateProjectOpen(true)}/>
            </div>
            <div class="project-list">
                {projects.length > 0 ? (
                    projects.map(project => (
                        <ProjectListItem key={project.id} project={project} />
                    ))
                ) : (
                    <p class="no-projects">No projects found</p>
                )}
            </div>
            {isCreateProjectOpen && <CreateProject onClose={() => setIsCreateProjectOpen(false) } onCreate={ createProject } />}
        </div>
    );
}
*/