#include "car.h"


MouseCar::MouseCar(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {

	rotationAngles.y = M_PI_2;
	scale = vec3(0.35, 0.35, 2.4);
	translation = vec3(0.3, 0, 0.8);
	position = vec3(26, 26, 0);

	Object* back = new Object(_shader, _material, _texture, new Tractricoid());
	back->scale = vec3(0.35, 0.35, 0.2);
	back->rotationAngles.y = M_PI_2;
	back->translation = vec3(2.7, 0, 0.8);
	children.push_back(back);

	tail = new Object(_shader, _material, _texture, new TruncatedCone(1.1, 0, 1));
	tail->scale = vec3(0.03, 0.03, 0.6);
	tail->rotationAngles.y = M_PI_2;
	tail->rotationAngles.x = M_PI_2 / 24;
	tail->translation = vec3(3.1, 0, 0.8);
	children.push_back(tail);

	Object* front = new Object(_shader, _material, _texture, new TruncatedCone(0.8, 0.4, 2));
	front->scale = vec3(0.3, 0.3, 0.4);
	front->rotationAngles.y = -M_PI_2;
	front->translation = vec3(-0.47, 0, 0.8);
	children.push_back(front);

	Sphere* sphere = new Sphere();
	nose = new Object(_shader, _material, new ColorTexture(1, 1, vec4(1, 0, 1, 1)), sphere);
	nose->scale = vec3(0.12, 0.12, 0.12);
	nose->translation = vec3(-1.33, 0, 0.8);
	lastNosePosition = position + translation + nose->translation;
	children.push_back(nose);

	ears = new Object(_shader, _material, _texture, new HourGlass());
	ears->scale = vec3(0.2, 0.25, 1.0);
	ears->rotationAngles.x = M_PI_2;
	ears->translation = vec3(0.4, 0, 1.1);
	children.push_back(ears);

	Texture* whitetex = new ColorTexture(1, 1, vec4(1, 1, 1, 1));
	Object* leftEye = new Object(_shader, _material, whitetex, sphere);
	leftEye->scale = vec3(0.12, 0.12, 0.12);
	leftEye->translation = vec3(0, -0.2, 1);
	children.push_back(leftEye);

	Object* rightEye = new Object(_shader, _material, whitetex, sphere);
	rightEye->scale = vec3(0.12, 0.12, 0.12);
	rightEye->translation = vec3(0, 0.2, 1);
	children.push_back(rightEye);

	Texture* stripetex = new StripedTexture(10, 10, 2, vec4(0.6, 0.8, 0.2, 1), vec4(0.6, 0.2, 0.8, 1));
	Torus* torus = new Torus();
	for (int i = 0; i < 4; i++) {
		Object* wheel = new Object(_shader, _material, stripetex, torus);
		wheel->scale = vec3(0.3, 0.3, 0.4);
		wheel->rotationAngles.x = M_PI_2;
		children.push_back(wheel);
		if (i < 2) {
			wheel->translation = vec3(0.8, -0.4, 0.4);
			leftwheels.push_back(wheel);
		}
		else {
			wheel->translation = vec3(0.8, 0.4, 0.4);
			rightwheels.push_back(wheel);
		}
		if (i % 2 == 0) wheel->translation.x = 2.2;
	}

	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->translateZ = children.at(i)->translation - translation;
	}
	createAuras();
}


void MouseCar::Animate(float time) {
	if (!alive) {
		geometry->Update(time);
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->geometry->Update(time);
		}
		return;
	}
	position = position + vec3(cosf(moveAngle), sinf(moveAngle), 0) * time * (leftWheelVelocity + rightWheelVelocity) / 2;
	moveAngle = moveAngle + time * (leftWheelVelocity - rightWheelVelocity) / 0.8;
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Move(position, moveAngle);
	}
	unsigned int wheelcnt = (leftwheels.size() < rightwheels.size()) ? leftwheels.size() : rightwheels.size();
	float leftmove = time * leftWheelVelocity * M_PI;
	float rightmove = time * rightWheelVelocity * M_PI;
	for (unsigned int i = 0; i < wheelcnt; i++) {
		leftwheels.at(i)->rotationAngles.y += leftmove;
		rightwheels.at(i)->rotationAngles.y += rightmove;
	}
	if (tailUp) tail->rotationAngles.y -= M_PI_4 / 4 * time;
	else tail->rotationAngles.y += M_PI_4 / 4 * time;
	if (tailUp && tail->rotationAngles.y < M_PI_2 * 11 / 12) tailUp = false;
	else if (!tailUp && tail->rotationAngles.y > M_PI_2 * 13 / 12) tailUp = true;
	if (tailLeft) tail->rotationAngles.x += M_PI_4 / 4 * time;
	else tail->rotationAngles.x -= M_PI_4 / 4 * time;
	if (tailLeft && tail->rotationAngles.x > M_PI_2 / 12) tailLeft = false;
	else if (!tailLeft && tail->rotationAngles.x < -M_PI_2 / 12) tailLeft = true;

	if (earLeft) ears->rotationAngles.x += M_PI_4 / 4 * time;
	else ears->rotationAngles.x -= M_PI_4 / 4 * time;
	if (earLeft && ears->rotationAngles.x > M_PI_2 * 13 / 12) earLeft = false;
	else if (!earLeft && ears->rotationAngles.x < M_PI_2 * 11 / 12) earLeft = true;
}

void MouseCar::createAuras() {
	vec3 rot = vec3(cosf(moveAngle), sinf(moveAngle), 0);
	vec3 center = position + translation;
	vec3 offset = -1.63 * rot;
	float radius = 0.12;
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = -1.1 * rot;
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = -0.6 * rot;
	radius = 0.4;
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = 0.1 * rot;
	radius = 0.5;
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = 0.6 * rot + vec3(0, 0, -0.4);
	radius = 0.8;
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = 1.3 * rot + vec3(0, 0, -0.4);
	auras.push_back(new SphereAura(center + offset, radius, center + offset));

	offset = 2 * rot + vec3(0, 0, -0.4);
	auras.push_back(new SphereAura(center + offset, radius, center + offset));
}

void MouseCar::updateAuras() {
	vec3 rot = vec3(cosf(moveAngle), sinf(moveAngle), 0);
	vec3 center = position + translation;
	vec3 offset = -1.63 * rot;
	auras.at(0)->lastposition = auras.at(0)->center;
	auras.at(0)->center = center + offset;

	offset = -1.1 * rot;
	auras.at(1)->lastposition = auras.at(1)->center;
	auras.at(1)->center = center + offset;

	offset = -0.6 * rot;
	auras.at(2)->lastposition = auras.at(2)->center;
	auras.at(2)->center = center + offset;

	offset = 0.1 * rot;
	auras.at(3)->lastposition = auras.at(3)->center;
	auras.at(3)->center = center + offset;

	offset = 0.6 * rot + vec3(0, 0, -0.4);
	auras.at(4)->lastposition = auras.at(4)->center;
	auras.at(4)->center = center + offset;

	offset = 1.3 * rot + vec3(0, 0, -0.4);
	auras.at(5)->lastposition = auras.at(5)->center;
	auras.at(5)->center = center + offset;

	offset = 2 * rot + vec3(0, 0, -0.4);
	auras.at(6)->lastposition = auras.at(6)->center;
	auras.at(6)->center = center + offset;
}

std::vector<SphereAura*> MouseCar::getAuras() {
	updateAuras();
	return auras;
}

void MouseCar::Collided(vec3 move) {
	if (!alive) return;
	normalize(move);
	position = position + (fabs((leftWheelVelocity+rightWheelVelocity)/2)+1) * move / 50;
	if (leftWheelVelocity > 0) leftWheelVelocity -= 0.2;
	else leftWheelVelocity += 0.2;
	if (rightWheelVelocity > 0) rightWheelVelocity -= 0.2;
	else rightWheelVelocity += 0.2;
	if (leftWheelVelocity <= 0.2 && leftWheelVelocity >= -0.2) leftWheelVelocity = 0;
	if (rightWheelVelocity <= 0.2 && rightWheelVelocity >= -0.2) rightWheelVelocity = 0;
}