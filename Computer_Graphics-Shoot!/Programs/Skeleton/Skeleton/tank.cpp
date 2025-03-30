#include "tank.h"

void Link::Animate(float time, float velocity) {
	l -= time * velocity;
	while (l >= 2 * R * (2 * linecnt + M_PI)) {
		l -= 2 * R * (2 * linecnt + M_PI);
	}
	if (l < 0) l += 2 * R * (2 * linecnt + M_PI);
	if (l < linecnt * R) {
		this->translation.x = l;
		this->translation.z = 0;
		this->rotationAngles.y = 0;
	}
	else if (l < R * (linecnt + M_PI)) {
		float dl = l - linecnt * R;
		float rot = dl / R;
		this->translation.x = R * (linecnt + sinf(rot));
		this->translation.z = R * (1 - cosf(rot));
		this->rotationAngles.y = -rot;
	}
	else if (l < R * (3 * linecnt + M_PI)) {
		this->translation.x = 2 * linecnt * R + M_PI * R - l;
		this->translation.z = 2 * R;
		this->rotationAngles.y = M_PI;
	}
	else if (l < R * (3 * linecnt + 2 * M_PI)) {
		float dl = l - 3 * linecnt * R;
		float rot = dl / R;
		this->translation.x = -R * linecnt + R * sinf(rot);
		this->translation.z = R * (1 - cosf(rot));
		this->rotationAngles.y = -rot;
	}
	else {
		this->translation.x = l - (4 * linecnt * R + 2 * M_PI * R);
		this->translation.z = 0;
		this->rotationAngles.y = 0;
	}
}

Chain::Chain(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry, float y, float w, float l, float d, float r, unsigned int c) {
	for (float i = 0; i < 2 * r * (2 * (c - 1) + M_PI); i += w + d) {
		Link* link = new Link(_shader, _material, _texture, _geometry, i, r, c);
		link->translation = vec3(-w / 2, y - l / 2, 0);
		link->scale = vec3(w, l, 1);
		links.push_back(link);
	}
	Sphere* sphere = new Sphere();
	Torus* torus = new Torus();
	Texture* darktexture = new ColorTexture(10, 10, vec4(0.1, 0.1, 0.1, 1));
	Texture* stripetexture = new StripedTexture(10, 10, 2, vec4(1, 1, 1, 1), vec4(0, 0, 0, 1));

	for (unsigned int i = 0; i < c; i++) {
		Object* wheelcenter = new Object(_shader, _material, darktexture, sphere);
		wheelcenter->scale = vec3(r / M_PI, r / M_PI, r / M_PI);
		wheelcenter->translation = vec3(-r * (c - 1) + i * 2 * r, y, r);
		wheelcenter->translateZ = wheelcenter->translation;
		wheels.push_back(wheelcenter);
		Object* wheel = new Object(_shader, _material, stripetexture, torus);
		wheel->translation = vec3(-r * (c - 1) + i * 2 * r, y, r);
		wheel->scale = vec3(r * 2 / 3, r * 2 / 3, l * 5 / 4);
		wheel->rotationAngles.x = M_PI_2;
		wheel->translateZ = wheel->translation;
		wheels.push_back(wheel);
	}
}

Body::Body(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {
	position = vec3(10, 19, 0);
	auras.push_back(new SphereAura(position + translation, 1.2, position+translation));

	Cylinder* cylinder = new Cylinder();
	turret = new Turret(_shader, _material, _texture, cylinder);
	turret->translation = vec3(0.0f, 0.0f, 0.3f);
	turret->scale = vec3(0.5f, 0.5f, 0.8f);

	Disk* disk = new Disk();
	Top* top = new Top(_shader, _material, _texture, disk);
	top->translation = vec3(0.0f, 0.0f, 1.1f);
	top->scale = vec3(0.5f, 0.5f, 1.0f);

	vec3 flagtranslation = vec3(-0.5 - 0.2, 0.5, 1.5);
	Shader* flagshader = new NPRShader();
	Material* flagmaterial = new Material;
	flagmaterial->diffuse = vec3(0.2f, 0.4f, 0.6f);
	flagmaterial->specular = vec3(0.4, 0.4, 0.4);
	flagmaterial->ambient = vec3(0.2f, 0.2f, 0.2f);
	flagmaterial->shininess = 30;
	vec4 red = vec4(1, 0, 0, 1), green = vec4(0, 1, 0, 1), white = vec4(1, 1, 1, 1);
	Texture* flagtex = new FlagTexture(15, 15, green, white, red);

	Object* flagrod = new Object(flagshader, flagmaterial, _texture, cylinder);
	flagrod->scale = vec3(0.01, 0.01, 1.2);
	flagrod->translation = vec3(-0.5, 0.5, 0.5);
	flagrod->translateZ = flagrod->translation - translation;
	children.push_back(flagrod);
	Object* flag1 = new Object(flagshader, flagmaterial, flagtex, new CurvedPlane(1, 1));
	flag1->scale = vec3(0.2, 0.15, 0.2);
	flag1->translation = flagtranslation;
	flag1->translateZ = flag1->translation - translation;
	children.push_back(flag1);
	Object* flag2 = new Object(flagshader, flagmaterial, flagtex, new CurvedPlane(1, 1));
	flag2->scale = vec3(0.1, 0.1, 0.2);
	flag2->translation = vec3(0.3, 0, 0) - flagtranslation;
	flag2->translation.z *= -1;
	flag2->rotationAngles.z = M_PI;
	flag2->translateZ = flag2->translation - translation;
	children.push_back(flag2);

	cannon = new Cannon(_shader, _material, _texture, cylinder);
	cannon->translation = vec3(0.3f, 0.0f, 0.9f);
	cannon->scale = vec3(0.1f, 0.1f, 1.5f);
	cannon->rotationAngles.y = M_PI_2;
	auras.push_back(new SphereAura(cannon->position + cannon->translation, 0.15, cannon->position + cannon->translation));

	children.push_back(turret);
	turret->children.push_back(top);
	turret->children.push_back(cannon);

	Plane* plane = new Plane();
	leftChain = new Chain(_shader, _material, _texture, plane, 0.75, 0.1, 0.4, 0.05, 0.3, 4);
	rightChain = new Chain(_shader, _material, _texture, plane, -0.75, 0.1, 0.4, 0.05, 0.3, 4);
}

void Body::Animate(float time) {
	for (unsigned int i = 0; i < balls.size(); i++) {
		balls.at(i)->Animate(time);
		if (balls.at(i)->position.z <= 0.001) {
			balls.erase(balls.begin() + i);
			i--;
		}
	}
	if (!alive) {
		geometry->Update(time);
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->geometry->Update(time);
		}
		for (unsigned int i = 0; i < leftChain->links.size(); i++) {
			leftChain->links.at(i)->geometry->Update(time);
		}
		for (unsigned int i = 0; i < leftChain->wheels.size(); i++) {
			leftChain->wheels.at(i)->geometry->Update(time);
		}
		for (unsigned int i = 0; i < rightChain->links.size(); i++) {
			rightChain->links.at(i)->geometry->Update(time);
		}
		for (unsigned int i = 0; i < rightChain->wheels.size(); i++) {
			rightChain->wheels.at(i)->geometry->Update(time);
		}
		return;
	}
	vec3 velocity = vec3(cosf(moveAngle), sinf(moveAngle), 0) * (leftChain->velocity + rightChain->velocity) / 2;
	position = position + velocity * time;
	moveAngle = moveAngle + time * (leftChain->velocity - rightChain->velocity) / 1.5;
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Move(position, moveAngle);
	}
	leftChain->Animate(time, position, moveAngle);
	rightChain->Animate(time, position, moveAngle);
}

void Body::updateAuras() {

	auras.at(0)->lastposition = auras.at(0)->center;
	auras.at(0)->center = position + translation;

	float moveA = moveAngle;
	if (turret->stabilized) moveA = turret->lastMoveAngle;
	vec3 cannonOffset = 0.3 * vec3(cosf(moveA + cannon->rotationAngles.z), sinf(moveA + cannon->rotationAngles.z), 3);
	vec3 cannonPosition = position + translation + cannonOffset;
	vec3 cannonDirection = vec3(
		cosf(moveA + cannon->rotationAngles.z) * sinf(cannon->rotationAngles.y),
		sinf(moveA + cannon->rotationAngles.z) * sinf(cannon->rotationAngles.y),
		cosf(cannon->rotationAngles.y)
	);
	vec3 cannonEnd = cannonPosition + cannonDirection * 1.5;
	auras.at(1)->lastposition = auras.at(1)->center;
	auras.at(1)->center = cannonEnd;
}

void Body::Shoot() {
	if (balls.size() > 15 || !alive) return;
	float moveA = moveAngle;
	if (turret->stabilized) moveA = turret->lastMoveAngle;
	vec3 cannonOffset = 0.3 * vec3(cosf(moveA + cannon->rotationAngles.z), sinf(moveA + cannon->rotationAngles.z), 3);
	vec3 cannonPosition = position + translation + cannonOffset;
	vec3 cannonDirection = vec3(
		cosf(moveA + cannon->rotationAngles.z) * sinf(cannon->rotationAngles.y),
		sinf(moveA + cannon->rotationAngles.z) * sinf(cannon->rotationAngles.y),
		cosf(cannon->rotationAngles.y)
	);
	vec3 cannonEnd = cannonPosition + cannonDirection * 1.5;
	vec3 velocity = normalize(cannonEnd - cannonPosition) * power;
	Projectile* ball = new Projectile(shader, material, texture, new Sphere, velocity, 1, cannonEnd);
	ball->scale = vec3(0.1, 0.1, 0.1);
	balls.push_back(ball);
}

bool Body::CheckProjectileCollision(Object* object) {
	std::vector<SphereAura*> objAuras = object->getAuras();
	bool hit = false;
	for (unsigned int i = 0; i < balls.size(); i++) {
		std::vector<SphereAura*> ballAuras = balls.at(i)->getAuras();
		for (unsigned int k = 0; k < ballAuras.size(); k++) {
			SphereAura* ballAura = ballAuras.at(k);
			for (unsigned int j = 0; j < objAuras.size(); j++) {
				SphereAura* aura = objAuras.at(j);
				if (length(aura->center - ballAura->center) <= aura->radius + ballAura->radius + 0.01) {
					balls.erase(balls.begin() + i);
					i--;
					return true;
				}
			}
		}
	}
	return false;
}

void Body::Explode(float time) {
	geometry->Explode(time);
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Explode(time);
	}
	leftChain->Explode(time);
	rightChain->Explode(time);
}

void Body::Collided(vec3 move) {
	if (!alive) return;
	normalize(move);
	position = position + (fabs((leftChain->velocity + rightChain->velocity) / 2) + 1) * move / 100;
	if (leftChain->velocity > 0) leftChain->velocity -= 0.2;
	else leftChain->velocity += 0.2;
	if (rightChain->velocity > 0) rightChain->velocity -= 0.2;
	else rightChain->velocity += 0.2;
	if (leftChain->velocity <= 0.2 && leftChain->velocity >= -0.2) leftChain->velocity = 0;
	if (rightChain->velocity <= 0.2 && rightChain->velocity >= -0.2) rightChain->velocity = 0;
}