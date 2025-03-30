//=============================================================================================
//  Sample Program: 3D engine-let
//
// NYILATKOZAT
// ---------------------------------------------------------------------------------------------
// Nev    : Tomori Peter Andras
// Neptun : I4RZ0O
// ---------------------------------------------------------------------------------------------
// ezennel kijelentem, hogy a feladatot magam keszitettem, es ha barmilyen segitseget igenybe vettem vagy
// mas szellemi termeket felhasznaltam, akkor a forrast es az atvett reszt kommentekben egyertelmuen jeloltem.
// A forrasmegjeloles kotelme vonatkozik az eloadas foliakat es a targy oktatoi, illetve a
// grafhazi doktor tanacsait kiveve barmilyen csatornan (szoban, irasban, Interneten, stb.) erkezo minden egyeb
// informaciora (keplet, program, algoritmus, stb.). Kijelentem, hogy a forrasmegjelolessel atvett reszeket is ertem,
// azok helyessegere matematikai bizonyitast tudok adni. Tisztaban vagyok azzal, hogy az atvett reszek nem szamitanak
// a sajat kontribucioba, igy a feladat elfogadasarol a tobbi resz mennyisege es minosege alapjan szuletik dontes.
// Tudomasul veszem, hogy a forrasmegjeloles kotelmenek megsertese eseten a hazifeladatra adhato pontokat
// negativ elojellel szamoljak el es ezzel parhuzamosan eljaras is indul velem szemben.
//=============================================================================================

#include "tank.h"

MouseCar* car;
Body* tank;

enum CameraType {
	RPS, FPS, Drone, Bird, Stopped
};

class Scene {
	std::vector<Object*> objects;
	std::vector<Light> lights;
	Ball* ball;
	Goal* goal;
	Camera camera;
	Camera carView;
	Camera tankView;
	
	void BuildLandscape(Shader* shader) {
		Material* landscapeMaterial = new Material;
		landscapeMaterial->diffuse = vec3(0.6f, 0.6f, 0.6f);
		landscapeMaterial->specular = vec3(0.1f, 0.1f, 0.1f);
		landscapeMaterial->ambient = vec3(0.2f, 0.2f, 0.2f);
		landscapeMaterial->shininess = 10;

		Texture* landTexture = new DirtTexture(2000, 2000);
		Texture* landscapeTexture = new DirtTexture(100, 100);
		Texture* buildingTexture = new SquaredTexture(50, 200, 8, vec4(0.9, 1, 0, 1), vec4(0.7, 0.7, 0.7, 1));

		Plane* plane = new Plane();
		Object* planeObject = new Object(shader, landscapeMaterial, landTexture, plane);
		planeObject->translation = vec3(-500.0f, -500.0f, -0.01f);
		planeObject->scale = vec3(1000, 1000, 1);
		objects.push_back(planeObject);

		std::vector<vec3> peaks;
		peaks.push_back(vec3(150, 50, 1.5)); peaks.push_back(vec3(30, 410, 4.5)); peaks.push_back(vec3(-35, 5, 2.8));
		peaks.push_back(vec3(5, -39, 1.1)); peaks.push_back(vec3(310, -41, 1.5)); peaks.push_back(vec3(20, -25, 2));
		peaks.push_back(vec3(180, 10, 8.5)); peaks.push_back(vec3(-30, -70, 5)); peaks.push_back(vec3(1, -21, 6));
		peaks.push_back(vec3(-100, 20, 1.8)); peaks.push_back(vec3(0, -55, 4.5)); peaks.push_back(vec3(-33, 33, 8.2));
		peaks.push_back(vec3(500, 500, 50)); peaks.push_back(vec3(-41, 14, 14)); peaks.push_back(vec3(98, 630, 30));
		peaks.push_back(vec3(75, -13, 10)); peaks.push_back(vec3(41, 14, 5)); peaks.push_back(vec3(90, 0, 3));
		float width = 4.0f, height = 1.5f, angle = M_PI_4;
		for (unsigned int i = 0; i < peaks.size(); i++) {
			vec3 peak = peaks.at(i);
			if (i % 2 == 0) width += 1;
			else height += 1;
			if (i % 4 == 1) width += cosf(angle);
			else if (i % 4 == 3) height += sinf(angle);
			angle += (M_PI_4 / 4);
			Pyramid* pyramid = new Pyramid(peak, peak.z, width, height);
			Object* pyramidObject = new Object(shader, landscapeMaterial, landscapeTexture, pyramid);
			objects.push_back(pyramidObject);
		}

		Box* box = new Box();
		Object* building0 = new Object(shader, landscapeMaterial, buildingTexture, box);
		building0->scale = vec3(8, 8, 40);
		building0->translation = vec3(11, -60, building0->scale.z/2);
		objects.push_back(building0);
		Object* building1 = new Object(shader, landscapeMaterial, buildingTexture, box);
		building1->scale = vec3(4, 4, 16);
		building1->translation = vec3(50, -15, building1->scale.z / 2);
		objects.push_back(building1);
		Object* building2 = new Object(shader, landscapeMaterial, buildingTexture, box);
		building2->scale = vec3(5, 5, 25);
		building2->translation = vec3(-80, 15, building1->scale.z / 2);
		objects.push_back(building2);
		Object* building3 = new Object(shader, landscapeMaterial, buildingTexture, box);
		building3->scale = vec3(10, 10, 30);
		building3->translation = vec3(100, 100, building1->scale.z / 2);
		objects.push_back(building3);
	}

public:
	CameraType camType = RPS;
	vec3 dronePosition;
	float droneLeft, droneUp;

	void Build() {
		carView.wVup = vec3(0, 0, 1);
		tankView.wVup = vec3(0, 0, 1);
		Shader* phongShader = new PhongShader();
		Shader* nprShader = new NPRShader();

		BuildLandscape(phongShader);

		Material* material0 = new Material;
		material0->diffuse = vec3(0.2f, 0.4f, 0.6f);
		material0->specular = vec3(0.4, 0.4, 0.4);
		material0->ambient = vec3(0.2f, 0.2f, 0.2f);
		material0->shininess = 30;

		Material* tankmaterial = new Material();
		tankmaterial->diffuse = vec3(0.4f, 0.4f, 0.4f);
		tankmaterial->specular = vec3(1.0f, 1.0f, 1.0f);
		tankmaterial->ambient = vec3(0.2f, 0.2f, 0.2f);
		tankmaterial->shininess = 5;

		vec3 peak = vec3(0.0f, 0.0f, 1.1f);
		float upheight = 0.8f, width = 2.0f, height = 1.5f;
		Pyramid* pyramid = new Pyramid(peak, upheight, width, height);

		tank = new Body(phongShader, tankmaterial, new NoisedColorTexture(150, vec3(0.1, 0.9, 0.4), true), pyramid);
		objects.push_back(tank);

		car = new MouseCar(nprShader, material0, new CheckerBoardTexture(15, 15, vec4(0.5, 0.2, 0.4, 1), vec4(0.4, 0.6, 0.8, 1)), new Cylinder);
		objects.push_back(car);
		
		ball = new Ball(nprShader, material0, new NoisedColorTexture(150, vec3(1, 0.2, 0.2), false), new Sphere());
		objects.push_back(ball);

		goal = new Goal(nprShader, material0, new ColorTexture(15, 15, vec4(0.5, 0.5, 0.5, 1)), new Box());
		objects.push_back(goal);

		// Lights
		lights.resize(1);
		lights[0].wLightPos = vec4(1, 2, 3, 0);	// ideal point -> directional light source
		lights[0].La = vec3(0.2f, 0.2f, 0.2f);
		lights[0].Le = vec3(1, 1, 1);
	}

	void Render() {
		RenderState state;
		state.wEye = camera.wEye;
		state.V = camera.V();
		state.P = camera.P();
		state.lights = lights;
		for (Object* obj : objects) obj->Draw(state);
	}

	void Animate(float time) {
		//Animating objects and collision detection
		for (unsigned int i = 0; i < objects.size(); i++) {
			objects.at(i)->Animate(time);
		}

		bool destroyed = tank->CheckProjectileCollision(car);
		if (destroyed) car->Die();

		car->CheckCollision(ball);
		tank->CheckCollision(ball);
		tank->CheckCollision(car);

		goal->CheckCollision(ball);
		bool scored=goal->CheckGoal(ball);
		if (scored) tank->Die();

		for (unsigned int i = 0; i < tank->balls.size(); i++) {
			tank->balls.at(i)->CheckCollision(ball);
		}

		//Animating tank camera
		if (camType == RPS) {
			vec3 dir = vec3(cosf(tank->moveAngle), sinf(tank->moveAngle), -1.5 / 5);
			tankView.wEye = tank->position + tank->translation - dir * 5;
			tankView.wLookat = tank->position + tank->translation + vec3(0, 0, 1.1);
		}
		else if (camType == FPS) {
			float moveAngle = tank->moveAngle;
			if (tank->turret->stabilized) moveAngle = tank->turret->lastMoveAngle;
			vec3 cannonOffset = 0.3 * vec3(cosf(moveAngle + tank->cannon->rotationAngles.z), sinf(moveAngle + tank->cannon->rotationAngles.z), 3);
			vec3 cannonPosition = tank->position + tank->translation + cannonOffset;
			vec3 cannonDirection = vec3(
				cosf(moveAngle + tank->cannon->rotationAngles.z) * sinf(tank->cannon->rotationAngles.y),
				sinf(moveAngle + tank->cannon->rotationAngles.z) * sinf(tank->cannon->rotationAngles.y),
				cosf(tank->cannon->rotationAngles.y)
			);
			vec3 cannonEnd = cannonPosition + cannonDirection * 1.5;
			tankView.wLookat = cannonEnd + vec3(0, 0, 0.6);

			cannonOffset.z = 0.3 * cosf(tank->cannon->rotationAngles.y);
			tankView.wEye = tank->position + tank->translation + vec3(0, 0, 2.0) - cannonOffset;
		}
		else if (camType == Drone) {
			tankView.wEye = dronePosition;
			tankView.wLookat = dronePosition + 2 * vec3(cosf(droneLeft)*cosf(droneUp), sinf(droneLeft), sinf(droneUp));
		}
		else if (camType == Bird) {
			tankView.wEye = vec3(tank->position.x, tank->position.y + 10, 10);
			tankView.wLookat = tank->position;
		}

		//Animating car camera
		vec3 cardir = vec3(cosf(car->moveAngle+M_PI), sinf(car->moveAngle+M_PI), -0.4);
		carView.wEye = car->position + car->translation -cardir * 8;
		carView.wLookat = car->position + car->translation + vec3(cardir.x, cardir.y, 1.5);
	}

	void switchView() {
		switch (camType) {
		case RPS: 
			camType = FPS;
			break;
		case FPS:
			camType = Drone;
			dronePosition = tank->position + vec3(-3, 0, 10);
			droneLeft = 0;
			droneUp = -M_PI_4;
			break;
		case Drone:
			camType = Bird;
			break;
		case Bird:
			camType = RPS;
			break;
		default: camType = RPS;
		}
	}
	void stopCam() {
		camType = Stopped;
	}
	void activateTankCamera() {
		camera = tankView;
	}
	void activateCarCamera() {
		camera = carView;
	}
};

Scene scene;

// Initialization, create an OpenGL context
void onInitialization() {
	printf("Tank jatek tovabbfejlesztese, Tomori Peter Andras I4RZ0O\n");
	printf("Jatekfunkciok:\n");

	printf("\nTank\n");
	printf(" Cel: eger lelovese\n");
	printf("\tBal lanc elore: A\n");
	printf("\tBal lanc hatra: a\n");
	printf("\tJobb lanc elore: D\n");
	printf("\tJobb lanc hatra: d\n");
	printf("\tLovegtorony balra: q vagy Q\n");
	printf("\tLovegtorony jobbra: e vagy E\n");
	printf("\tLogevtorony stabilizator be: y vagy Y\n");
	printf("\tLogevtorony stabilizator ki: x vagy X\n");
	printf("\tAgyu fel: w vagy W\n");
	printf("\tAgyu le: s vagy S\n");
	printf("\tLoves: szokoz vagy bal klikk\n");
	printf("\tLoves ero novel: 3\n");
	printf("\tLoves ero csokken: 1\n");
	printf("\tKamera valtas: c (Nezetek: RPS, FPS, Dron, Madartavlat)\n");
	printf("\tKamera megallitas: C\n");

	printf("\nDron nezet iranyitas\n");
	printf(" Cel: nezelodes\n");
	printf("\tVissza a tankhoz: ,\n");
	printf("\tElore - hatra mozgas: 8 - 2\n");
	printf("\tBalra - jobbra mozgas: 4 - 6\n");
	printf("\tFuggoleges fel - le mozgas: 5 - 0\n");
	printf("\tFel es le nezes: + es -\n");
	printf("\tBalra es jobbra forgas: 7 es 9\n");

	printf("\nEger\n");
	printf(" Cel: labda kapuba guritasa, lovedekek elkerulese\n");
	printf("\tGyorsitas: Fel nyil\n");
	printf("\tLassitas es tolatas: Le nyil\n");
	printf("\tJobbra gyorsitas: Jobb nyil\n");
	printf("\tBalra gyorsitas: Bal nyil\n");

	glEnable(GL_DEPTH_TEST);
	glDisable(GL_CULL_FACE);
	scene.Build();
}

// Window has become invalid: Redraw
void onDisplay() {
	glClearColor(0.5f, 0.5f, 0.8f, 1.0f);							// background color 
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the screen

	int halfWidth = windowWidth / 2;
	glViewport(0, 0, halfWidth, windowHeight);
	scene.activateTankCamera();
	scene.Render();

	glViewport(halfWidth, 0, halfWidth, windowHeight);
	scene.activateCarCamera();
	scene.Render();
	
	glutSwapBuffers(); // exchange the two buffers
}


// Key of ASCII code pressed
void onKeyboard(unsigned char key, int pX, int pY) { 
	switch (key) {
	case 'Q':
	case 'q':
		tank->rotateTurretHorizontal(M_PI_4/3.0f);
		break;
	case 'E':
	case 'e':
		tank->rotateTurretHorizontal(-M_PI_4 / 3.0f);
		break;
	case 'W':
	case 'w':
		if(tank->cannon->rotationAngles.y > 0.001) tank->rotateCannonVertical(-M_PI_4 / 9.0f);
		break;
	case 'S':
	case 's':
		if (tank->cannon->rotationAngles.y < M_PI_2+M_PI_4/4) tank->rotateCannonVertical(M_PI_4 / 9.0f);
		break;
	case 'D':
		tank->speedRight(0.1);
		break;
	case 'd':
		tank->speedRight(-0.1);
		break;
	case 'A':
		tank->speedLeft(0.1);
		break;
	case 'a':
		tank->speedLeft(-0.1);
		break;
	case 'c':
		scene.switchView();
		break;
	case 'C':
		scene.stopCam();
		break;
	case '8':
		if (scene.camType == Drone) scene.dronePosition = scene.dronePosition + vec3(cosf(scene.droneLeft) * cosf(scene.droneUp), sinf(scene.droneLeft), sinf(scene.droneUp)) * 0.5;
		break;
	case '2':
		if (scene.camType == Drone)  scene.dronePosition = scene.dronePosition - vec3(cosf(scene.droneLeft) * cosf(scene.droneUp), sinf(scene.droneLeft), sinf(scene.droneUp)) * 0.5;
		break;
	case '4':
		if (scene.camType == Drone) scene.dronePosition = scene.dronePosition + vec3(-sinf(scene.droneLeft) * cosf(scene.droneUp), cosf(scene.droneLeft), 0) * 0.5;
		break;
	case '6':
		if (scene.camType == Drone) scene.dronePosition = scene.dronePosition - vec3(-sinf(scene.droneLeft) * cosf(scene.droneUp), cosf(scene.droneLeft), 0) * 0.5;
		break;
	case '5':
		if (scene.camType == Drone) scene.dronePosition.z += 0.5;
		break;
	case '0':
		if (scene.camType == Drone) scene.dronePosition.z -= 0.5;
		break;
	case '7':
		if (scene.camType == Drone) scene.droneLeft += M_PI_4 / 2;
		break;
	case '9':
		if (scene.camType == Drone) scene.droneLeft -= M_PI_4 / 2;
		break;
	case '+':
		if (scene.camType == Drone) scene.droneUp += M_PI_4 / 2;
		break;
	case '-':
		if (scene.camType == Drone) scene.droneUp -= M_PI_4 / 2;
		break;
	case ',':
		if (scene.camType == Drone) {
			scene.dronePosition = tank->position + vec3(-3, 0, 10);
			scene.droneLeft = 0;
			scene.droneUp = -M_PI_4;
		}
		break;
	case ' ':
		tank->Shoot();
		break;
	case '3':
		if(tank->power >= 5) tank->power += 5;
		else tank->power++;
		break;
	case '1':
		if (tank->power > 5) tank->power -= 5;
		else if (tank->power > 2) tank->power--;
		break;
	case 'y':
	case 'Y':
		if (!tank->turret->stabilized) {
			tank->turret->stabilized = true;
			tank->turret->lastMoveAngle = tank->turret->moveAngle;
		}
		break;
	case 'x':
	case 'X':
		tank->turret->stabilized = false;
		break;
	default:
		break;
	}
	glutPostRedisplay();
}

// Key of ASCII code released
void onKeyboardUp(unsigned char key, int pX, int pY) { }

void onSpecialKey(int key, int pX, int pY)
{
	switch (key)
	{
	case GLUT_KEY_LEFT:
		car->rightWheelVelocity -=0.1;
		break;
	case GLUT_KEY_RIGHT:
		car->leftWheelVelocity -= 0.1;
		break;
	case GLUT_KEY_UP:
		car->leftWheelVelocity -= 0.1;
		car->rightWheelVelocity -= 0.1;
		break;
	case GLUT_KEY_DOWN:
		car->leftWheelVelocity += 0.1;
		car->rightWheelVelocity += 0.1;
		break;
	default:
		break;
	}
	glutPostRedisplay();
}

// Mouse click event
void onMouse(int button, int state, int pX, int pY) { 
	if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN) {
		tank->Shoot();
	}
}

// Move mouse with key pressed
void onMouseMotion(int pX, int pY) {
}

// Idle event indicating that some time elapsed: do animation here
static float lastDisplay = 0;
void onIdle() {
	float time = glutGet(GLUT_ELAPSED_TIME) / 1000.0f;
	float timeElapsed = time - lastDisplay;
	lastDisplay = time;
	scene.Animate(timeElapsed);
	glutPostRedisplay();
}