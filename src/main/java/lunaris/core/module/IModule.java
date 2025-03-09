package lunaris.core.module;

public interface IModule {
    void onEnable();
    void onDisable();
    String getName();
    ModuleInfo getInfo();
} 