#pragma once

#include "geometry.h"

struct SphereAura {
	vec3 center;
	float radius;
	vec3 lastposition;
	SphereAura(vec3 c, float r, vec3 lp) : center(c), radius(r), lastposition(lp) {}
};

const float m0 = 0.3;
const float g = 1.62f;

struct Object {
protected:
	std::vector<SphereAura*> auras;
public:
	bool alive = true;
	Shader* shader;
	Material* material;
	Texture* texture;
	Geometry* geometry;
	vec3 scale, translation;
	vec3 rotationAngles;
	vec3 translateX, translateY, translateZ;
	std::vector<Object*> children;
	vec3 position;
	float moveAngle;

	Object(Shader* _shader, Material* _material, Texture* _texture, Geometry* _geometry) :
		scale(vec3(1, 1, 1)), translation(vec3(0, 0, 0)), rotationAngles(vec3(0, 0, 0)),
		translateX(vec3(0, 0, 0)), translateY(vec3(0, 0, 0)), translateZ(vec3(0, 0, 0)),
		position(vec3(0, 0, 0)), moveAngle(0) {
		shader = _shader;
		texture = _texture;
		material = _material;
		geometry = _geometry;
	}

	virtual void SetModelingTransform(mat4& M, mat4& Minv) {
		mat4 rotX = TranslateMatrix(translateX) * RotationMatrix(rotationAngles.x, vec3(1, 0, 0)) * TranslateMatrix(-translateX);
		mat4 rotY = TranslateMatrix(translateY) * RotationMatrix(rotationAngles.y, vec3(0, 1, 0)) * TranslateMatrix(-translateY);
		mat4 rotZ = TranslateMatrix(translateZ) * RotationMatrix(rotationAngles.z + moveAngle, vec3(0, 0, 1)) * TranslateMatrix(-translateZ);
		M = ScaleMatrix(scale) * rotX * rotY * rotZ * TranslateMatrix(translation + position);
		mat4 invRot = RotationMatrix(-rotationAngles.z - moveAngle, vec3(0, 0, 1)) * RotationMatrix(-rotationAngles.y, vec3(0, 1, 0)) * RotationMatrix(-rotationAngles.x, vec3(1, 0, 0));
		Minv = TranslateMatrix(-translation - position) * invRot * ScaleMatrix(vec3(1 / scale.x, 1 / scale.y, 1 / scale.z));
	}

	virtual void Draw(RenderState state);

	virtual void Animate(float time);

	virtual void Move(vec3 pos, float angle);

	virtual void rotateTurretHorizontal(float angle);

	virtual void rotateCannonVertical(float angle);

	virtual void updateAuras() {}

	virtual std::vector<SphereAura*> getAuras() {
		updateAuras();
		return auras;
	}

	virtual void Collided(vec3 move) { }

	virtual void CheckCollision(Object* object);

	virtual void Die() {
		if (!alive) return;
		alive = false;
		Explode(0.4);
	}

	virtual void Explode(float time);
};