#pragma once

#include "shader.h"

struct VertexData {
	vec3 position, normal;
	vec2 texcoord;
};

class Geometry {
protected:
	unsigned int vao, vbo;       // vertex array object
	std::vector<VertexData> vtxData;
	std::vector<vec3> velocities;
public:
	float remainingTime = 5;
	Geometry() {
		glGenVertexArrays(1, &vao);
		glBindVertexArray(vao);
		glGenBuffers(1, &vbo); // Generate 1 vertex buffer object
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	virtual void Draw() = 0;
	~Geometry() {
		glDeleteBuffers(1, &vbo);
		glDeleteVertexArrays(1, &vao);
	}

	void Explode(float time) {
		velocities.clear();
		remainingTime = time;
		for (size_t i = 0; i < vtxData.size() / 3; ++i) {
			vec3 velocity = 0.5*normalize(vec3(((float)rand() / RAND_MAX - 0.5f) * 2.0f, ((float)rand() / RAND_MAX - 0.5f) * 2.0f, ((float)rand() / RAND_MAX - 0.5f) * 2.0f));
			velocities.push_back(velocity);
		}
	}

	void Update(float time) {
		if (remainingTime <= 0) return;
		remainingTime -= time;
		for (size_t i = 0; i < velocities.size(); ++i) {
				for (size_t j = 0; j < 3; ++j) {
					vtxData[i * 3 + j].position = vtxData[i * 3 + j].position + velocities[i] * time;
				}
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vtxData.size() * sizeof(VertexData), &vtxData[0]);
	}
};

class VertexSurface : public Geometry {
public:
	void create() {

		glBufferData(GL_ARRAY_BUFFER, vtxData.size() * sizeof(VertexData), &vtxData[0], GL_STATIC_DRAW);
		// Enable the vertex attribute arrays
		glEnableVertexAttribArray(0);  // attribute array 0 = POSITION
		glEnableVertexAttribArray(1);  // attribute array 1 = NORMAL
		glEnableVertexAttribArray(2);  // attribute array 2 = TEXCOORD0
		// attribute array, components/attribute, component type, normalize?, stride, offset
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, position));
		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, normal));
		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, texcoord));
	}

	void Draw() {
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vtxData.size());
	}
};

class Pyramid : public VertexSurface {
	vec3 p;
	float up, w, h;
	std::vector<vec3> vtx;

	std::vector<VertexData> GenVertexDatas() {
		std::vector<VertexData> vtxDatas;
		for (unsigned int i = 0; i < vtx.size(); i++) {
			VertexData vtxData;
			vtxData.position = vtx[i];
			if (i % 3 == 0) {
				vtxData.texcoord = vec2(0.0f, 0.0f);
				vtxData.normal = normalize(cross(vtx[i + 1] - vtxData.position, vtx[i + 2] - vtxData.position));
			}
			else {
				if (i % 3 == 1) vtxData.texcoord = vec2(0.5f, 0.5f);
				else vtxData.texcoord = vec2(1.0f, 0.0f);
				vtxData.normal = vtxDatas[i - 1].normal;
			}
			vtxDatas.push_back(vtxData);
		}
		return vtxDatas;
	}

public:
	Pyramid(vec3 peak, float upheight, float width, float height) {
		p = peak;
		up = upheight;
		w = width;
		h = height;

		float x = p.x, y = p.y, z = p.z - up;
		vtx.push_back(p);
		vtx.push_back(vec3(x - w / 2, y - h / 2, z));
		vtx.push_back(vec3(x - w / 2, y + h / 2, z));

		vtx.push_back(p);
		vtx.push_back(vec3(x - w / 2, y + h / 2, z));
		vtx.push_back(vec3(x + w / 2, y + h / 2, z));

		vtx.push_back(p);
		vtx.push_back(vec3(x + w / 2, y + h / 2, z));
		vtx.push_back(vec3(x + w / 2, y - h / 2, z));

		vtx.push_back(p);
		vtx.push_back(vec3(x + w / 2, y - h / 2, z));
		vtx.push_back(vec3(x - w / 2, y - h / 2, z));

		vtxData = GenVertexDatas();

		create();
	}
};


class Box : public VertexSurface {
private:
	void AddVertexDatas(vec3 vtxs[4]) {

		vec3 normal = normalize(cross(vtxs[1] - vtxs[0], vtxs[2] - vtxs[0]));

		for (int i = 0; i < 3; i++) {
			VertexData vertex;
			vertex.position = vtxs[i];
			vertex.normal = normal;
			if (i == 0) vertex.texcoord = vec2(0, 0);
			else if (i == 1) vertex.texcoord = vec2(1, 0);
			else vertex.texcoord = vec2(1, 1);
			vtxData.push_back(vertex);
		}
		for (int i = 0; i < 4; i++) {
			if (i == 1) continue;
			VertexData vertex;
			vertex.position = vtxs[i];
			vertex.normal = normal;
			if (i == 0) vertex.texcoord = vec2(0, 0);
			else if (i == 2) vertex.texcoord = vec2(1, 1);
			else vertex.texcoord = vec2(0, 1);
			vtxData.push_back(vertex);
		}
	}
protected:
	std::vector<VertexData> GenVertexDatas() {
		return vtxData;
	}
public:

	Box() {
		float s = 0.5f;
		vec3 allvtx[8] = { vec3(-s, -s, -s), vec3(s, -s, -s), vec3(s, s, -s), vec3(-s, s, -s),
			vec3(-s, -s, s), vec3(s, -s, s), vec3(s, s, s), vec3(-s, s, s) };
		vec3 actvtx[4];
		actvtx[0] = allvtx[0]; actvtx[1] = allvtx[1]; actvtx[2] = allvtx[2]; actvtx[3] = allvtx[3]; AddVertexDatas(actvtx);
		actvtx[0] = allvtx[4]; actvtx[1] = allvtx[5]; actvtx[2] = allvtx[6]; actvtx[3] = allvtx[7]; AddVertexDatas(actvtx);
		actvtx[0] = allvtx[0]; actvtx[1] = allvtx[1]; actvtx[2] = allvtx[5]; actvtx[3] = allvtx[4]; AddVertexDatas(actvtx);
		actvtx[0] = allvtx[2]; actvtx[1] = allvtx[3]; actvtx[2] = allvtx[7]; actvtx[3] = allvtx[6]; AddVertexDatas(actvtx);
		actvtx[0] = allvtx[0]; actvtx[1] = allvtx[3]; actvtx[2] = allvtx[7]; actvtx[3] = allvtx[4]; AddVertexDatas(actvtx);
		actvtx[0] = allvtx[1]; actvtx[1] = allvtx[2]; actvtx[2] = allvtx[6]; actvtx[3] = allvtx[5]; AddVertexDatas(actvtx);
		create();
	}
};

class ParamSurface : public Geometry {
	unsigned int nVtxPerStrip, nStrips;
public:
	ParamSurface() { nVtxPerStrip = nStrips = 0; }

	virtual void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) = 0;

	VertexData GenVertexData(float u, float v) {
		VertexData vtxData;
		vtxData.texcoord = vec2(u, v);
		Dual2 X, Y, Z;
		Dual2 U(u, vec2(1, 0)), V(v, vec2(0, 1));
		eval(U, V, X, Y, Z);
		vtxData.position = vec3(X.value, Y.value, Z.value);
		vec3 drdU(X.deriv.x, Y.deriv.x, Z.deriv.x), drdV(X.deriv.y, Y.deriv.y, Z.deriv.y);
		vtxData.normal = cross(drdU, drdV);
		return vtxData;
	}

	void create(int N = tessellationLevel, int M = tessellationLevel) {
		nVtxPerStrip = (M + 1) * 2;
		nStrips = N;
		vtxData.clear();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j <= M; j++) {
				vtxData.push_back(GenVertexData((float)j / M, (float)i / N));
				vtxData.push_back(GenVertexData((float)j / M, (float)(i + 1) / N));
			}
		}
		glBufferData(GL_ARRAY_BUFFER, nVtxPerStrip * nStrips * sizeof(VertexData), &vtxData[0], GL_STATIC_DRAW);
		// Enable the vertex attribute arrays
		glEnableVertexAttribArray(0);  // attribute array 0 = POSITION
		glEnableVertexAttribArray(1);  // attribute array 1 = NORMAL
		glEnableVertexAttribArray(2);  // attribute array 2 = TEXCOORD0
		// attribute array, components/attribute, component type, normalize?, stride, offset
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, position));
		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, normal));
		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(VertexData), (void*)offsetof(VertexData, texcoord));
	}

	void Draw() {
		glBindVertexArray(vao);
		for (unsigned int i = 0; i < nStrips; i++) glDrawArrays(GL_TRIANGLE_STRIP, i * nVtxPerStrip, nVtxPerStrip);
	}
};

class Plane : public ParamSurface {
public:
	Plane() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		X = U; Y = V; Z = 0;
	}
};

class Disk : public ParamSurface {
public:
	Disk() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * M_PI; V = V * 2 - 1.0f;
		X = Cos(U) * V; Y = Sin(U) * V; Z = 0;
	}
};

class Cylinder : public ParamSurface {
public:
	Cylinder() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * M_PI;
		X = Cos(U); Y = Sin(U); Z = V;
	}
};

class Torus : public ParamSurface {
public:
	Torus() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		const float R = 1, r = 0.5f;
		U = U * 2.0f * M_PI, V = V * 2.0f * M_PI;
		Dual2 D = Cos(U) * r + R;
		X = D * Cos(V); Y = D * Sin(V); Z = Sin(U) * r;
	}
};

class Sphere : public ParamSurface {
public:
	Sphere() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * (float)M_PI, V = V * (float)M_PI;
		X = Cos(U) * Sin(V); Y = Sin(U) * Sin(V); Z = Cos(V);
	}
};

class Tractricoid : public ParamSurface {
public:
	Tractricoid() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		const float height = 3.0f;
		U = U * height, V = V * 2 * M_PI;
		X = Cos(V) / Cosh(U); Y = Sin(V) / Cosh(U); Z = U - Tanh(U);
	}
};

class HourGlass : public ParamSurface {
public:
	HourGlass() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * M_PI, V = V * 2.0f - 1.0f;
		X = V * Cos(U); Y = V * Sin(U); Z = V;
	}
};

class TruncatedCone : public ParamSurface {
public:
	float r1, r2, h;
	TruncatedCone(float r1_, float r2_, float h_) : r1(r1_), r2(r2_), h(h_) { create(); }

	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * M_PI; V = V * 2 - 1.0f;
		X = Cos(U) * (r1 * (1 - V.value) + r2 * V.value);
		Y = Sin(U) * (r1 * (1 - V.value) + r2 * V.value);
		Z = V * h;
	}
};

class CurvedPlane : public ParamSurface {
public:
	float radius, height;
	CurvedPlane(float radius_, float height_) : radius(radius_), height(height_) { create(); }

	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * M_PI;
		V = V * 2 - 1;

		X = Cos(U) * radius;
		Y = Sin(U) * radius;
		Z = V * height;
	}
};

class TestSurface : public ParamSurface {
public:
	TestSurface(float amplitude, float frequency, const vec3& windDirection)
		: amplitude_(amplitude), frequency_(frequency), windDirection_(windDirection) {
		create();
	}
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 2.0f * M_PI;
		V = V * M_PI;
		float widthRatio = 0.8f + 0.2f * V.value;
		X = Cos(U) * widthRatio + V * windDirection_.x;
		Y = Sin(U) + V * windDirection_.y;
		float wavingFactor = V.value * -1 + 1;
		Z = V * amplitude_ * wavingFactor * Sin(U * frequency_ + V * windDirection_.z);
	}
private:
	float amplitude_;
	float frequency_;
	vec3 windDirection_;
};



class Mobius : public ParamSurface {
public:
	Mobius() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		const float R = 1, width = 0.5f;
		U = U * M_PI, V = (V - 0.5f) * width;
		X = (Cos(U) * V + R) * Cos(U * 2);
		Y = (Cos(U) * V + R) * Sin(U * 2);
		Z = Sin(U) * V;
	}
};

class Klein : public ParamSurface {
	const float size = 1.5f;
public:
	Klein() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * M_PI * 2, V = V * M_PI * 2;
		Dual2 a = Cos(U) * (Sin(U) + 1) * 0.3f;
		Dual2 b = Sin(U) * 0.8f;
		Dual2 c = (Cos(U) * (-0.1f) + 0.2f);
		X = a + c * ((U.value > M_PI) ? Cos(V + M_PI) : Cos(U) * Cos(V));
		Y = b + ((U.value > M_PI) ? 0 : c * Sin(U) * Cos(V));
		Z = c * Sin(V);
	}
};

class Boy : public ParamSurface {
public:
	Boy() { create(); }
	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = (U - 0.5f) * M_PI, V = V * M_PI;
		float r2 = sqrt(2.0f);
		Dual2 denom = (Sin(U * 3) * Sin(V * 2) * (-3 / r2) + 3) * 1.2f;
		Dual2 CosV2 = Cos(V) * Cos(V);
		X = (Cos(U * 2) * CosV2 * r2 + Cos(U) * Sin(V * 2)) / denom;
		Y = (Sin(U * 2) * CosV2 * r2 - Sin(U) * Sin(V * 2)) / denom;
		Z = (CosV2 * 3) / denom;
	}
};

class Dini : public ParamSurface {
	Dual2 a = 1.0f, b = 0.15f;
public:
	Dini() { create(); }

	void eval(Dual2& U, Dual2& V, Dual2& X, Dual2& Y, Dual2& Z) {
		U = U * 4 * M_PI, V = V * (1 - 0.1f) + 0.1f;
		X = a * Cos(U) * Sin(V);
		Y = a * Sin(U) * Sin(V);
		Z = a * (Cos(V) + Log(Tan(V / 2))) + b * U + 3;
	}
};