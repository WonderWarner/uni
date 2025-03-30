#include "Object.h"


void Object::Draw(RenderState state) {
	if (!alive&&geometry->remainingTime<0) return;
	mat4 M, Minv;
	SetModelingTransform(M, Minv);
	state.M = M;
	state.Minv = Minv;
	state.MVP = state.M * state.V * state.P;
	state.material = material;
	state.texture = texture;
	shader->Bind(state);
	geometry->Draw();
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Draw(state);
	}
}

void Object::Animate(float time) {
	if (!alive) {
		geometry->Update(time);
		for (unsigned int i = 0; i < children.size(); i++) {
			children.at(i)->geometry->Update(time);
		}
		return;
	}
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Animate(time);
	}
}

void Object::Move(vec3 pos, float angle) {
	if (!alive) return;
	position = pos;
	moveAngle = angle;
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Move(pos, angle);
	}
}

void Object::rotateTurretHorizontal(float angle) {
	if (!alive) return;
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->rotateTurretHorizontal(angle);
	}
}
void Object::rotateCannonVertical(float angle) {
	if (!alive) return;
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->rotateCannonVertical(angle);
	}
}

void Object::CheckCollision(Object* object) {
	std::vector<SphereAura*> objauras = object->getAuras();
	updateAuras();
	for (unsigned int i = 0; i < auras.size(); i++) {
		SphereAura* aura = auras.at(i);
		for (unsigned int j = 0; j < objauras.size(); j++) {
			SphereAura* objaura = objauras.at(j);
			if (length(objaura->center - aura->center) <= aura->radius + objaura->radius + 0.01) {
				vec3 v = aura->center - aura->lastposition;
				vec3 dirv = objaura->center - aura->center;
				vec3 moveVector = 2*(v+dirv);
				moveVector.z = 0;
				object->Collided(moveVector);
				return;
			}
		}
	}
}

void Object::Explode(float time) {
	geometry->Explode(time);
	for (unsigned int i = 0; i < children.size(); i++) {
		children.at(i)->Explode(time);
	}
}