package br.github.hebertfsoares.homeplugin.commands;

import br.github.hebertfsoares.homeplugin.Homeplugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.UUID;

/**
 * Classe responsável pelo comando /home que permite aos jogadores teleportarem para suas homes.
 *
 * <p>Este comando tem um cooldown configurável e exibe partículas e sons ao teletransportar.</p>
 */
public class HomeCommand implements CommandExecutor {
    private final Homeplugin plugin;

    /**
     * Construtor para a classe HomeCommand.
     *
     * @param plugin A instância do plugin Homeplugin que está associada a este comando.
     */
    public HomeCommand(Homeplugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executa o comando /home.
     *
     * <p>O comando verifica se o jogador está em cooldown, e se não estiver, inicia um contador para o teletransporte.</p>
     * <p>Quando o teletransporte é concluído, o jogador é teleportado para a home definida, e partículas e sons são exibidos.</p>
     *
     * @param sender O remetente do comando. Deve ser um jogador.
     * @param command O comando executado.
     * @param label O rótulo do comando.
     * @param args Argumentos fornecidos com o comando.
     * @return {@code true} se o comando foi executado com sucesso, {@code false} caso contrário.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar esse comando.");
            return true;
        }
        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        HashMap<UUID, Long> cooldowns = plugin.getCooldowns();
        long cooldownTime = plugin.getConfig().getInt("cooldown") * 1000L;

        // Verifica se o jogador está em cooldown
        if (cooldowns.containsKey(playerId)) {
            long lastUsed = cooldowns.get(playerId);
            long timeLeft = (cooldownTime - (System.currentTimeMillis() - lastUsed)) / 1000;
            if (System.currentTimeMillis() - lastUsed < cooldownTime) {
                String cooldownMessage = plugin.getConfig().getString("messages.cooldown_wait").replace("{time}", Long.toString(timeLeft));
                player.sendMessage(ChatColor.YELLOW + cooldownMessage);
                return true;
            }
        }

        Location homeLocation = plugin.getHome(player);
        if (homeLocation != null) {
            BukkitRunnable teleportTask = new BukkitRunnable() {
                int countdown = plugin.getConfig().getInt("countdown_time");

                @Override
                public void run() {
                    if (countdown > 0) {
                        String teleportingMessage = plugin.getConfig().getString("messages.teleporting").replace("{time}", Integer.toString(countdown));
                        player.sendMessage(ChatColor.RED + teleportingMessage);
                        countdown--;
                    } else {
                        player.teleport(homeLocation);
                        cooldowns.put(playerId, System.currentTimeMillis());
                        player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messages.teleport_success"));
                        if (plugin.getConfig().getBoolean("show_particles")) {
                            player.getWorld().spawnParticle(Particle.CLOUD, homeLocation, 100);
                        }
                        player.playSound(homeLocation, Sound.ITEM_GOAT_HORN_SOUND_0, 1.0F, 1.0F);
                        plugin.getTeleportTasks().remove(playerId);
                        cancel();
                    }
                }
            };
            plugin.getTeleportTasks().put(playerId, teleportTask);
            teleportTask.runTaskTimer(plugin, 0, 20);
        } else {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no_home_set"));
        }
        return true;
    }
}
