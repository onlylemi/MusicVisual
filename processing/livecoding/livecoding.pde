import ddf.minim.*;
import ddf.minim.analysis.*;

ArrayList<Particle> particles;

void setup() {
	size(1280, 720, P3D);
	frameRate(30);
	background(0);
	initAudio();

	particles = new ArrayList<Particle>();
	for (int i = 0; i < 1024; i++) {
		particles.add(new Particle(random(width), random(height)));
	}
}

void draw() {
	noStroke();
	fill(0, audio.mix.level() * 255);
	rect(0, 0, width, height);

	fft.forward(audio.mix);

	noStroke();
	for (int i = 0; i < particles.size(); i++) {
		Particle p = particles.get(i);
		p.maxSpeed = 1 + audio.mix.level() * fft.getBand(i) + 1;
		p.attraction(new PVector(width / 2, height / 2));
		p.repulsion(new PVector(width / 2, height / 2));
		p.wander(1);
		p.update();
		p.dispaly();
	}

	noStroke();
	fill(255, 25);
	float radius = audio.left.level() * 1000;
	ellipse(width / 2, height / 2, radius, radius);

	radius = audio.right.level() * 1500;
	ellipse(width / 2, height / 2, radius, radius);
}