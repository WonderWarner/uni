#pragma once
#include "gameengine.h"

class CheckerBoardTexture : public Texture {
public:
	CheckerBoardTexture(const int width, const int height, vec4 color1, vec4 color2);
};

class DirtTexture : public Texture {
public:
	DirtTexture(const int width, const int height);
};

class StripedTexture : public Texture {
public:
	StripedTexture(const int width, const int height, const int stripe_width, vec4 color1, vec4 color2);
};

class SquaredTexture : public Texture {
public:
	SquaredTexture(const int width, const int height, const int square_width, vec4 color1, vec4 color2);
};

class FlagTexture : public Texture {
	vec4 bottomcolor, middlecolor, topcolor;
	float width, length;
public:
	FlagTexture(float width, float length, const vec4& bottom, const vec4& mid, const vec4& top);
};

class ColorTexture : public Texture {
public:
	ColorTexture(const int width, const int height, vec4 color);
};

//PerlinNoise Source: https://solarianprogrammer.com/2012/07/18/perlin-noise-cpp-11/
#include <cmath>
#include <random>
#include <algorithm>
#include <numeric>
class PerlinNoise {
	// The permutation vector
	std::vector<int> p;
public:
	// Initialize with the reference values for the permutation vector
	PerlinNoise();
	// Generate a new permutation vector based on the value of seed
	PerlinNoise(unsigned int seed);
	// Get a noise value, for 2D images z can have any value
	double noise(double x, double y, double z);
private:
	double fade(double t);
	double lerp(double t, double a, double b);
	double grad(int hash, double x, double y, double z);
};

class NoisedColorTexture : public Texture {
public:
	NoisedColorTexture(int width, const vec3& baseColor, bool wood);
private:
	vec3 color;
	bool wood;
	std::vector<vec4> generateImage(int width);
};

