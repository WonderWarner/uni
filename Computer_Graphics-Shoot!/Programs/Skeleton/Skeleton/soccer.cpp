#include "soccer.h"


Ball::Ball(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {
	scale = vec3(0.8, 0.8, 0.8);
	position = vec3(20, 20, 0.8);
	auras.push_back(new SphereAura(position + translation, scale.x, position + translation));
}

void Ball::Animate(float time) {
	if (!alive) {
		geometry->Update(time);
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->geometry->Update(time);
		}
		return;
	}
	vec3 frictionForce = vec3(-m0 * mass * g * time, -m0 * mass * g * time, 0.0f);
	velocity = velocity + velocity * frictionForce;
	vec3 move = velocity * time;
	position = position + move;

	float angle = length(move) / scale.x;
	vec3 direction = velocity;
	if (length(velocity) > 0.0001) direction = normalize(velocity);

	rotationAngles.x += direction.y * angle;
	rotationAngles.y += direction.x * angle;
}

void Ball::updateAuras() {
	auras.at(0)->lastposition = auras.at(0)->center;
	auras.at(0)->center = translation + position;
}

void Ball::Collided(vec3 move) {
	velocity = velocity + move;
}


Goal::Goal(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {
	scale = vec3(0.2, 0.2, goalHeight);
	translation = vec3(15, 12, goalHeight / 2);
	auras.push_back(new SphereAura(vec3(translation.x, translation.y, 0.8), 0.3, vec3(translation.x, translation.y, 0.8)));

	Object* leftWall = new Object(*this);
	leftWall->translation.x += goalWidth;
	children.push_back(leftWall); partCnt++;
	auras.push_back(new SphereAura(vec3(leftWall->translation.x, leftWall->translation.y, 0.8), 0.3, vec3(leftWall->translation.x, leftWall->translation.y, 0.8)));

	Object* upWall = new Object(_shader, _material, _texture, _geometry);
	upWall->scale = scale;
	upWall->scale.z = goalWidth + scale.x;
	upWall->rotationAngles.y = M_PI_2;
	upWall->translation = translation + vec3(upWall->scale.z / 2 - scale.x / 2, 0, goalHeight / 2);
	children.push_back(upWall); partCnt++;

	Object* rightUpWall = new Object(_shader, _material, _texture, _geometry);
	rightUpWall->scale = scale;
	rightUpWall->scale.z = goalDepth;
	rightUpWall->rotationAngles.x = M_PI_2;
	rightUpWall->translation = translation + vec3(0, -rightUpWall->scale.z / 2 - scale.y / 2, goalHeight / 2);
	children.push_back(rightUpWall); partCnt++;

	Object* leftUpWall = new Object(*rightUpWall);
	leftUpWall->translation = translation + vec3(goalWidth, -leftUpWall->scale.z / 2 - scale.y / 2, goalHeight / 2);
	children.push_back(leftUpWall); partCnt++;

	Object* rightDownWall = new Object(_shader, _material, _texture, _geometry);
	rightDownWall->scale = scale;
	rightDownWall->scale.z = goalHeight / sinf(backAngle);
	rightDownWall->rotationAngles.x = M_PI_2 + backAngle;
	rightDownWall->translation = translation + vec3(0, -rightDownWall->scale.z / 2 * sinf(backAngle) - goalDepth, 0);
	children.push_back(rightDownWall); partCnt++;

	Object* leftDownWall = new Object(*rightDownWall);
	leftDownWall->translation.x += goalWidth;
	children.push_back(leftDownWall); partCnt++;

	Texture* ropetex = new ColorTexture(15, 15, vec4(0.9, 0.9, 0.9, 1));
	Cylinder* cylinder = new Cylinder();
	vec3 ropeScale = vec3(0.01, 0.01, rightDownWall->scale.z);
	float ropeRot = rightDownWall->rotationAngles.x;
	vec3 ropePoz = translation + vec3(0, -goalDepth, goalHeight / 2);
	for (float i = 0.1; i < goalWidth; i += 0.1) {
		Object* backrope = new Object(_shader, _material, ropetex, cylinder);
		backrope->scale = ropeScale;
		backrope->rotationAngles.x = ropeRot;
		backrope->translation = ropePoz;
		backrope->translation.x += i;
		children.push_back(backrope); partCnt++;

		Object* uprope = new Object(_shader, _material, ropetex, cylinder);
		uprope->scale = vec3(0.01, 0.01, goalDepth);
		uprope->rotationAngles.x = M_PI / 2;
		uprope->translation = translation + vec3(0, 0, goalHeight / 2);
		uprope->translation.x += i;
		children.push_back(uprope); partCnt++;
	}
	for (float i = 0.1; i < goalDepth; i += 0.1) {
		Object* rightsiderope = new Object(_shader, _material, ropetex, cylinder);
		rightsiderope->scale = vec3(0.01, 0.01, goalHeight);
		rightsiderope->translation.x = translation.x;
		rightsiderope->translation.y = translation.y - i;
		children.push_back(rightsiderope); partCnt++;

		Object* leftsiderope = new Object(*rightsiderope);
		leftsiderope->translation.x += goalWidth;
		children.push_back(leftsiderope); partCnt++;
	}
	for (float i = 0; i < rightDownWall->scale.z * sinf(backAngle); i += 0.1) {
		Object* rightsiderope = new Object(_shader, _material, ropetex, cylinder);
		rightsiderope->scale = vec3(0.01, 0.01, sinf(M_PI_2 - backAngle) * (rightDownWall->scale.z * sinf(backAngle) - i) / sinf(backAngle));
		rightsiderope->translation.x = translation.x;
		rightsiderope->translation.y = translation.y - goalDepth - i;
		children.push_back(rightsiderope); partCnt++;

		Object* leftsiderope = new Object(*rightsiderope);
		leftsiderope->translation.x += goalWidth;
		children.push_back(leftsiderope); partCnt++;
	}
}

bool Goal::CheckGoal(Ball* ball) {
	std::vector<SphereAura*> objauras = ball->getAuras();
	for (unsigned int i = 0; i < objauras.size(); i++) {
		vec3 ballPos = objauras.at(i)->center;
		if (ballPos.x > translation.x && ballPos.x < translation.x + goalWidth && ballPos.y < translation.y && ballPos.y > translation.y - goalDepth) {
			scored = true;
			return true;
		}
	}
	return false;
}

void Goal::Animate(float time) {
	if (!scored) return;
	geometry->Update(time);
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->geometry->Update(time);
	}
	if (animationCnt == 0) {
		Geometry* dini = new Dini();
		Texture* tex = new CheckerBoardTexture(10, 10, vec4(1, 1, 0, 1), vec4(0, 0, 1, 1));
		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++) {
			Object* sparkle = new Object(shader, material, tex, dini);
			sparkle->scale = vec3(0.2, 0.2, 0.2);
			sparkle->translation = translation + vec3(goalWidth / 2, 0, 0);
			sparkle->rotationAngles.x = -(10 - j) * M_PI / 10;
			sparkle->rotationAngles.z = M_PI_2 - ((float)i)/5 * M_PI_2;
			children.push_back(sparkle);
		}
	}
	else if (animationCnt < 50) {
		for (unsigned int i = partCnt; i < children.size(); i++) {
			children.at(i)->translation = children.at(i)->translation + time * ((float)animationCnt+30)/15 * vec3(-sinf(children.at(i)->rotationAngles.z), -sinf(children.at(i)->rotationAngles.x)*cosf(children.at(i)->rotationAngles.z), cosf(children.at(i)->rotationAngles.x));
			children.at(i)->rotationAngles.y += 0.1;
		}
	}
	else if (animationCnt == 50) {
		children.erase(children.begin()+partCnt, children.end());
	}
	if (animationCnt < 51) animationCnt++;
}