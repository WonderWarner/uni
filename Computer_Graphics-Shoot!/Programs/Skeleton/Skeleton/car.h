#pragma once
#include "soccer.h"

struct MouseCar : Object {
	Object* nose;
	vec3 lastNosePosition;
	Object* ears;
	bool earLeft = true;
	Object* tail;
	bool tailUp = true;
	bool tailLeft = true;
	std::vector<Object*> leftwheels;
	std::vector<Object*> rightwheels;
	float leftWheelVelocity = 0;
	float rightWheelVelocity = 0;

	MouseCar(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry);

	void speedLeft(float v) {
		leftWheelVelocity += v;
	}
	void speedRight(float v) {
		rightWheelVelocity += v;
	}

	void Animate(float time);

	void createAuras();

	void updateAuras();

	void Collided(vec3 move);

	std::vector<SphereAura*> getAuras();
};