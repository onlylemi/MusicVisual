package livecoding;

import java.util.ArrayList;
import java.util.List;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Music Visual , Live coding by YuanRen in OF COURSE
 * 
 * @author only乐秘
 *
 */
public class LiveCoding extends PApplet {

	private Minim minim;
	private AudioPlayer audio;
	private List<Particle> particles;
	private FFT fft;
	private String[] lrcs = { "Say something, I'm giving up on you.",
			"I'll be the one, if you want me to.",
			"Anywhere, I would've followed you.",
			"Say something, I'm giving up on you.",
			"And I am feeling so small.", "It was over my head",
			"I know nothing at all.", "And I will stumble and fall.",
			"I'm still learning to love", "Just starting to crawl.",
			"Say something, I'm giving up on you.",
			"I’m sorry that I couldn’t get to you",
			"Anywhere, I would've followed you.",
			"Say something, I'm giving up on you.",
			"And I will swallow my pride.", "You're the one that I love",
			"And I'm saying goodbye.", "Say something, I'm giving up on you.",
			"And I'm sorry that I couldn't get to you.",
			"And anywhere, I would have followed you.",
			"Oh-oh-oh-oh say something, I'm giving up on you.",
			"Say something, I'm giving up on you.", "Say something..." };

	public void settings() {
		size(1280, 720, P3D);
	}

	public void setup() {
		frameRate(30);
		background(0);
		initAudio();

		particles = new ArrayList<Particle>();
		for (int i = 0; i < 1024; i++) {
			particles.add(new Particle(random(width), random(height)));
		}
	}

	public void draw() {
		noStroke();
		fill(0, audio.mix.level() * 255);
		rect(0, 0, width, height);

		fft.forward(audio.mix);

		noStroke();
		// 粒子绘制
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			p.setMaxSpeed(1 + audio.mix.level() * fft.getBand(i) + 1);
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

	public static void main(String _args[]) {
		PApplet.main(new String[] { livecoding.LiveCoding.class.getName() });
	}

	/**
	 * 初始化音乐
	 */
	public void initAudio() {
		minim = new Minim(this);
		audio = minim.loadFile("Say Something.mp3", 1024); // 加载音乐文件
		audio.play(80000);

		fft = new FFT(audio.bufferSize(), audio.sampleRate());
	}

	/**
	 * 粒子类
	 * 
	 * @author only乐秘
	 *
	 */
	class Particle {
		private PVector pos, vel, acc;
		private float maxSpeed = 1;
		private String lyric = "";

		public Particle(float x, float y) {
			pos = new PVector(x, y);
			vel = new PVector();
			acc = new PVector();

			if (random(360) <= 120) {
				lyric = lrcs[(int) random(lrcs.length)];
			}
		}

		public void wander(float range) {
			PVector force = PVector.random2D();
			force.setMag(random(range));
			applyForce(force);
		}

		/**
		 * 引力
		 * 
		 * @param target
		 */
		public void attraction(PVector target) {
			PVector force = PVector.sub(target, pos);
			float d = force.mag();
			if (d < 500) {
				force.setMag(d * 0.003f);
				applyForce(force);
			}
		}

		/**
		 * 斥力
		 * 
		 * @param target
		 */
		public void repulsion(PVector target) {
			PVector force = PVector.sub(pos, target);
			float d = force.mag();
			if (d < 400) {
				force.setMag(audio.mix.level() * 1000 / d);
				applyForce(force);
			}
		}

		/**
		 * 受力
		 * 
		 * @param force
		 *            力向量
		 */
		public void applyForce(PVector force) {
			acc.add(force);
		}

		/**
		 * 位置、速度、加速度的更新
		 */
		public void update() {
			vel.add(acc);
			vel.limit(maxSpeed);
			pos.add(vel);
			acc.mult(0);
		}

		/**
		 * 显示
		 */
		public void dispaly() {
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

		public float getMaxSpeed() {
			return maxSpeed;
		}

		public void setMaxSpeed(float maxSpeed) {
			this.maxSpeed = maxSpeed;
		}

	}
}
