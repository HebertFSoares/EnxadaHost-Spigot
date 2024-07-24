package br.github.hebertfsoares.windChargePL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import br.github.hebertfsoares.windChargePL.listeners.WindChargeListener;

/**
 * Plugin principal para o WindCharge.
 *
 * <p>Este plugin permite que os jogadores interajam com um Wind Charge que lança projéteis de vento,
 * causando efeitos especiais em jogadores próximos.</p>
 */
public final class WindCharge extends JavaPlugin {

    /**
     * Método chamado quando o plugin é ativado.
     *
     * <p>Registra o listener de eventos e carrega a configuração padrão do plugin.</p>
     */
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Wind Charge Plugin Ativado!");

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new WindChargeListener(this), this);
    }

    /**
     * Método chamado quando o plugin é desativado.
     *
     * <p>Realiza a limpeza necessária quando o plugin é desativado.</p>
     */
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Wind Charge Plugin Desativado!");
    }
}
