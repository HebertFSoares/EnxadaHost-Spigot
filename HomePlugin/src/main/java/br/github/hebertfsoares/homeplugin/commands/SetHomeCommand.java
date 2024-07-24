package br.github.hebertfsoares.homeplugin.commands;

import br.github.hebertfsoares.homeplugin.Homeplugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;

/**
 * Classe responsável pelo comando /sethome que permite aos jogadores definir sua home atual.
 *
 * <p>Este comando salva a localização atual do jogador como sua home.</p>
 */
public class SetHomeCommand implements CommandExecutor {
    private final Homeplugin plugin;

    /**
     * Construtor para a classe SetHomeCommand.
     *
     * @param plugin A instância do plugin Homeplugin que está associada a este comando.
     */
    public SetHomeCommand(Homeplugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executa o comando /sethome.
     *
     * <p>O comando permite que um jogador defina sua localização atual como sua home. Se o remetente não for um jogador, uma mensagem de erro é enviada.</p>
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
        Location location = player.getLocation();
        plugin.setHome(player, location);
        player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messages.home_set"));
        return true;
    }
}
