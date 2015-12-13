class Particle {
	PVector pos, vel, acc;
	float maxSpeed = 1;
	String lyric = "";

	Particle(float x, float y) {
		pos = new PVector(x, y);
		vel = new PVector();
		acc = new PVector();

		if (random(360) <= 120) {
			lyric = lrcs[(int) random(lrcs.length)];
		}
	}

	void wander(float range) {
		PVector force = PVector.random2D();
		force.setMag(random(range));
		applyForce(force);
	}

	void attraction(PVector target) {
		PVector force = PVector.sub(target, pos);
		float d = force.mag();
		if (d < 500) {
			force.setMag(d * 0.003f);
			applyForce(force);
		}
	}

	void repulsion(PVector target) {
		PVector force = PVector.sub(pos, target);
		float d = force.mag();
		if (d < 400) {
			force.setMag(audio.mix.level() * 1000 / d);
			applyForce(force);
		}
	}

	void applyForce(PVector force) {
		acc.add(force);
	}

	void update() {
		vel.add(acc);
		vel.limit(maxSpeed);
		pos.add(vel);
		acc.mult(0);
	}


	void dispaly() {

		noFill();
		stroke(255);
		strokeWeight(vel.mag());
		point(pos.x, pos.y);

		if ("" != lyric && maxSpeed > 5) {
			fill(255);
			float size = maxSpeed / 0.8f;
			textSize(size);
			text(lyric, pos.x - lyric.length() * size / 3, pos.y);
		}

	}
}