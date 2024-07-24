package br.github.hebertfsoares.homeplugin.listeners;

import br.github.hebertfsoares.homeplugin.Homeplugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Classe responsável por ouvir eventos de movimento de jogadores.
 *
 * <p>Esta classe cancela a tarefa de teletransporte se o jogador se mover durante o processo de teletransporte.</p>
 */
public class PlayerMoveListener implements Listener {
    private final Homeplugin plugin;

    /**
     * Construtor para a classe PlayerMoveListener.
     *
     * @param plugin A instância do plugin Homeplugin que está associada a este listener.
     */
    public PlayerMoveListener(Homeplugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Manipula o evento de movimento do jogador.
     *
     * <p>Se o jogador estiver no meio de um teletransporte e se mover, a tarefa de teletransporte será cancelada e o jogador receberá uma mensagem informando sobre o cancelamento.</p>
     *
     * @param event O evento de movimento do jogador.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (plugin.getTeleportTasks().containsKey(playerId)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                BukkitRunnable task = plugin.getTeleportTasks().remove(playerId);
                if (task != null) {
                    task.cancel();
                    player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.teleport_cancelled"));
                }
            }
        }
    }
}
