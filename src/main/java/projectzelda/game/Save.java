package projectzelda.game;

import projectzelda.engine.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.HashMap;

public class Save {
    QuestState questState;
    WeaponState weaponState;
    List<GameObject> gameObjects = new LinkedList<GameObject>();
    Avatar avatar;

    public Save(QuestState questState_, WeaponState weaponState_, List<GameObject> originalGameObjects) {
        questState = questState_;
        weaponState = weaponState_;
        for (GameObject go : originalGameObjects) {
            GameObject clone = go.clone();
            gameObjects.add(clone);
        }

        // We have to specially fix linked objects
        for (GameObject clone : gameObjects) {
            Const.Type type = Const.Type.values()[clone.type()];
            switch(type) {
                case AVATAR:
                    avatar = (Avatar)clone;
                    break;
                case NPC:
                    NPC n = (NPC)clone;
                    if (n instanceof BobNpc) {
                        BobNpc b = (BobNpc)n;
                        b.cat = (CatNpc)findReplacement(b.cat, originalGameObjects, gameObjects);
                        b.dog = (DogNpc)findReplacement(b.dog, originalGameObjects, gameObjects);
                    }
                    if (n.getFollow() == null) { break; }
                    GameObject r = findReplacement(n.getFollow(), originalGameObjects, gameObjects);
                    n.setFollow(r);
                    break;
                case GOBLIN:
                    EnemyAI b = (EnemyAI)clone;
                    b.target = findReplacement(b.target, originalGameObjects, gameObjects);
                    break;
                case SHOT:
                    if (!(clone instanceof MonsterOrb)) {
                        MonsterOrb m = (MonsterOrb)clone;
                        m.target = findReplacement(m.target, originalGameObjects, gameObjects);
                        break;
                    }
                    if (!(clone instanceof VoidOrb)) {
                        VoidOrb v = (VoidOrb)clone;
                        v.parent = findReplacement(v.parent, originalGameObjects, gameObjects);
                        break;
                    }
                    break; 
            }
        }
    }

    public static GameObject findReplacement(GameObject target, List<GameObject> original, List<GameObject> clones) {
        for (int i = 0; i < original.size(); i++) {
            if (target == original.get(i)) {
                return clones.get(i);
            }
        }
        return null;
    }
}
