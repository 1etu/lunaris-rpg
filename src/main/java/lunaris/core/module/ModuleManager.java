package lunaris.core.module;

import lunaris.core.Lunaris;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModuleManager {
    @SuppressWarnings("unused")
    private final Lunaris plugin;
    private final Map<String, IModule> modules;

    public ModuleManager(Lunaris plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
    }

    public void registerModule(IModule module) {
        ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
        if (info == null) {
            throw new IllegalArgumentException(module.getClass().getName() + " where's @ModuleInfo? :D");
        }

        for (String dependency : info.dependencies()) {
            if (!modules.containsKey(dependency)) {
                throw new IllegalStateException(module.getClass().getName() + " requires " + dependency + " which is not registered");
            }
        } 

        modules.put(info.name(), module);
    }

    public void enableModules() {
        modules.values().forEach(IModule::onEnable);
    }

    public void disableModules() {
        modules.values().forEach(IModule::onDisable);
    }

    public void enableModule(IModule module) {
        module.onEnable();
    }

    public Optional<IModule> getModule(String name) {
        return Optional.ofNullable(modules.get(name));
    }

    @SuppressWarnings("unchecked")
    public <T extends IModule> Optional<T> getModule(Class<T> moduleClass) {
        return modules.values().stream()
                .filter(module -> moduleClass.isAssignableFrom(module.getClass()))
                .map(module -> (T) module)
                .findFirst();
    }
} 