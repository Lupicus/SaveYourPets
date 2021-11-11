var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI')

function initializeCoreMod() {
    return {
    	'HorseEntity': {
    		'target': {
    			'type': 'CLASS',
    			'name': 'net.minecraft.world.entity.animal.horse.Horse'
    		},
    		'transformer': function(classNode) {
    			if (classNode.superName == "net/minecraft/world/entity/animal/horse/AbstractHorse") {
    				classNode.superName = "com/lupicus/syp/entity/DyingHorseEntity"
    				for (var i = 0; i < classNode.methods.size(); ++i) {
    					var obj = classNode.methods.get(i)
    					if (obj.name == "<init>") {
    						fixSuperCall(obj)
    					}
    				}
    			}
    			else
    				asmapi.log("WARN", "Horse might die (" + classNode.superName + ")")
    			return classNode
    		}
    	}
    }
}

function fixSuperCall(obj) {
	var call = asmapi.findFirstMethodCall(obj, asmapi.MethodType.SPECIAL, "net/minecraft/world/entity/animal/horse/AbstractHorse", "<init>", "(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V")
	if (call != null)
		call.owner = "com/lupicus/syp/entity/DyingHorseEntity"
}
