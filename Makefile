LAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	  projectzelda/Const.java \
	  projectzelda/engine/Frame.java \
	  projectzelda/engine/GameObject.java \
	  projectzelda/engine/GameObjectList.java \
	  projectzelda/engine/GraphicSystem.java \
	  projectzelda/engine/InputSystem.java \
	  projectzelda/engine/PhysicsSystem.java \
	  projectzelda/engine/TextObject.java \
	  projectzelda/engine/UserInput.java \
	  projectzelda/engine/World.java \
	  projectzelda/game/Avatar.java \
	  projectzelda/game/CounterGrenades.java \
	  projectzelda/game/Counter.java \
	  projectzelda/game/Grenade.java \
	  projectzelda/game/HelpText.java \
	  projectzelda/game/RPGPhysicsSystem.java \
	  projectzelda/game/RPGWorld.java \
	  projectzelda/game/Shot.java \
	  projectzelda/game/Tree.java \
	  projectzelda/game/ZombieAI.java \
	  projectzelda/gfx/AWTInputSystem.java \
	  projectzelda/gfx/SwingFrame.java \
	  projectzelda/gfx/SwingPanel.java \
	  projectzelda/Main.java

default: classes run

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: 
	java projectzelda/Main
