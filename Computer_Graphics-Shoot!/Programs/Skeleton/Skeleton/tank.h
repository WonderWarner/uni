#pragma once

#include "car.h"

struct Turret : Object {
	bool stabilized = false;
	float lastMoveAngle = 0;
	Turret(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {}
	void rotateTurretHorizontal(float angle) {
		rotationAngles.z = rotationAngles.z + angle;
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->rotateTurretHorizontal(angle);
		}
	}

	void Move(vec3 pos, float angle) {
		position = pos;
		if (!stabilized) {
			moveAngle = angle;
			for (unsigned int i = 0; i < children.size(); i++) {
				children.at(i)->Move(pos, angle);
			}
		}
		else {
			for (unsigned int i = 0; i < children.size(); i++) {
				children.at(i)->Move(pos, children.at(i)->moveAngle);
			}
		}
	}
};

struct Top : Object {
	Top(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {}
	void rotateTurretHorizontal(float angle) {
		rotationAngles.z = rotationAngles.z + angle;
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->rotateTurretHorizontal(angle);
		}
	}
};

struct Cannon : Object {
	Cannon(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) : Object(_shader, _material, _texture, _geometry) {
		translateZ = vec3(0.3, 0, 0);
	}
	void rotateTurretHorizontal(float angle) {
		rotationAngles.z = rotationAngles.z + angle;
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->rotateTurretHorizontal(angle);
		}
	}
	void rotateCannonVertical(float angle) {
		rotationAngles.y = rotationAngles.y + angle;
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->rotateCannonVertical(angle);
		}
	}
};

struct Projectile : Object {
	vec3 velocity;
	float mass;
	Projectile(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry, const vec3& velocity, float mass, vec3 pos)
		: Object(_shader, _material, _texture, _geometry), velocity(velocity), mass(mass) {
		this->position = pos;
		auras.push_back(new SphereAura(position + translation, scale.x, position + translation));
	}

	void Animate(float time) {
		if (!alive) return;
		vec3 gravityForce = vec3(0.0f, 0.0f, -g * mass * time);
		velocity = velocity + gravityForce;
		position = position + velocity * time;
	}

	void updateAuras() {
		auras.at(0)->lastposition = auras.at(0)->center;
		auras.at(0)->center = position + translation;
	}
};


struct Link : Object {
	float l, R;
	unsigned int linecnt;
	Link(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry, float l, float R, unsigned int cnt)
		: Object(_shader, _material, _texture, _geometry), l(l), R(R), linecnt(cnt - 1) {}

	void Animate(float time, float velocity);

	void Move(vec3 pos, float angle) {
		position = pos;
		moveAngle = angle;
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->Move(pos, angle);
		}
		translateZ = translation;
	}
};

struct Chain {
	std::vector<Link*> links;
	std::vector<Object*> wheels;
	float velocity = 0;

	Chain(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry, float y, float w, float l, float d, float r, unsigned int c);

	void Draw(RenderState state) {
		for (unsigned int i = 0; i < links.size(); i++) {
			links.at(i)->Draw(state);
		}
		for (unsigned int i = 0; i < wheels.size(); i++) {
			wheels.at(i)->Draw(state);
		}
	}
	void Animate(float time, vec3 pos, float angle) {
		for (unsigned int i = 0; i < links.size(); i++) {
			links.at(i)->Move(pos, angle);
			links.at(i)->Animate(time, velocity);
		}
		float move = time * velocity * M_PI;
		for (unsigned int i = 0; i < wheels.size(); i++) {
			wheels.at(i)->Move(pos, angle);
			wheels.at(i)->rotationAngles.y += move;
		}
	}

	void Chain::Explode(float time) {
		for (unsigned int i = 0; i < links.size(); i++) {
			links.at(i)->Explode(time);
		}
		for (unsigned int i = 0; i < wheels.size(); i++) {
			wheels.at(i)->Explode(time);
		}
	}
};

struct Body : Object {
	Chain* leftChain;
	Chain* rightChain;
	Cannon* cannon;
	Turret* turret;
	std::vector<Projectile*> balls;
	float power = 5;

	Body(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry);

	void speedLeft(float v) {
		leftChain->velocity += v;
	}
	void speedRight(float v) {
		rightChain->velocity += v;
	}

	void Animate(float time);

	void Draw(RenderState state) {
		if (!alive&&geometry->remainingTime<0) return;
		Object::Draw(state);
		leftChain->Draw(state);
		rightChain->Draw(state);
		for (unsigned int i = 0; i < balls.size(); i++) {
			balls.at(i)->Draw(state);
		}
	}

	void updateAuras();

	void Shoot();

	bool CheckProjectileCollision(Object* object);

	void Explode(float time);

	void Collided(vec3 move);
};

