#pragma once

#include "Object.h"

struct Ball : Object {
	vec3 velocity = vec3(0, 0, 0);
	float mass = 1;
	Ball(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry);

	void Animate(float time);
	void Collided(vec3 move);
	void updateAuras();
};

struct Goal : Object {
private:
	float goalWidth = 6;
	float goalHeight = 3;
	float goalDepth = 1.5;
	float backAngle = M_PI_4;
	bool scored = false;
	int animationCnt = 0;
	int partCnt=0;
public:
	Goal(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry);
	bool CheckGoal(Ball* ball);
	void Animate(float time);
};