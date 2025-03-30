#pragma once

#include "framework.h"

template<class T> struct Dual {
	float value;
	T deriv;
	Dual(float v0 = 0, T d0 = T(0)) { value = v0, deriv = d0; }
	Dual operator+(Dual r) { return Dual(value + r.value, deriv + r.deriv); }
	Dual operator-(Dual r) { return Dual(value - r.value, deriv - r.deriv); }
	Dual operator*(Dual r) {
		return Dual(value * r.value, value * r.deriv + deriv * r.value);
	}
	Dual operator/(Dual r) {
		return Dual(value / r.value, (r.value * deriv - r.deriv * value) / (r.value * r.value));
	}
};

template<class T> Dual<T> Exp(Dual<T> g) { return Dual<T>(expf(g.value), expf(g.value) * g.deriv); }
template<class T> Dual<T> Sin(Dual<T> g) { return  Dual<T>(sinf(g.value), cosf(g.value) * g.deriv); }
template<class T> Dual<T> Cos(Dual<T>  g) { return  Dual<T>(cosf(g.value), -sinf(g.value) * g.deriv); }
template<class T> Dual<T> Tan(Dual<T>  g) { return Sin(g) / Cos(g); }
template<class T> Dual<T> Sinh(Dual<T> g) { return  Dual<T>(sinh(g.value), cosh(g.value) * g.deriv); }
template<class T> Dual<T> Cosh(Dual<T> g) { return  Dual<T>(cosh(g.value), sinh(g.value) * g.deriv); }
template<class T> Dual<T> Tanh(Dual<T> g) { return Sinh(g) / Cosh(g); }
template<class T> Dual<T> Log(Dual<T> g) { return  Dual<T>(logf(g.value), g.deriv / g.value); }
template<class T> Dual<T> Pow(Dual<T> g, float n) { return  Dual<T>(powf(g.value, n), n * powf(g.value, n - 1) * g.deriv); }

typedef Dual<vec2> Dual2;

const int tessellationLevel = 50;

struct Camera {
	vec3 wEye, wLookat, wVup;   // extrinsic
	float fov, asp, fp, bp;		// intrinsic
public:
	Camera() {
		asp = (float)windowWidth / 2 / windowHeight;
		fov = 45.0f * (float)M_PI / 180.0f;
		fp = 1; bp = 200;
	}
	mat4 V() { // view matrix: translates the center to the origin
		vec3 w = normalize(wEye - wLookat);
		vec3 u = normalize(cross(wVup, w));
		vec3 v = cross(w, u);
		return TranslateMatrix(wEye * (-1)) * mat4(u.x, v.x, w.x, 0,
			u.y, v.y, w.y, 0,
			u.z, v.z, w.z, 0,
			0, 0, 0, 1);
	}

	mat4 P() { // projection matrix
		return mat4(1 / (tan(fov / 2) * asp), 0, 0, 0,
			0, 1 / tan(fov / 2), 0, 0,
			0, 0, -(fp + bp) / (bp - fp), -1,
			0, 0, -2 * fp * bp / (bp - fp), 0);
	}
};

struct Material {
	vec3 diffuse, specular, ambient;
	float shininess;
};

struct Light {
	vec3 La, Le;
	vec4 wLightPos; // homogeneous coordinates, can be at ideal point
};

struct RenderState {
	mat4 MVP, M, Minv, V, P;
	Material* material;
	std::vector<Light> lights;
	Texture* texture;
	vec3 wEye;
};