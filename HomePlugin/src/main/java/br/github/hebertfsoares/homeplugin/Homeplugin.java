package br.github.hebertfsoares.homeplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import br.github.hebertfsoares.homeplugin.commands.SetHomeCommand;
import br.github.hebertfsoares.homeplugin.commands.HomeCommand;
import br.github.hebertfsoares.homeplugin.listeners.PlayerMoveListener;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * O plugin Homeplugin para Minecraft, desenvolvido para definir e teletransportar para home dos jogadores.
 *
 * <p>Este plugin utiliza um banco de dados MySQL para armazenar as informações de home dos jogadores.</p>
 *
 * <p>Os principais recursos incluem:</p>
 * <ul>
 *     <li>Definir uma home para o jogador.</li>
 *     <li>Teletransportar o jogador para a home definida.</li>
 *     <li>Cooldown configurável entre comandos.</li>
 *     <li>Exibir partículas ao teletransportar.</li>
 * </ul>
 *
 * <p>O plugin suporta a configuração de banco de dados através do arquivo config.yml.</p>
 */
public final class Homeplugin extends JavaPlugin {

    private HashMap<UUID, Long> cooldowns;
    private HashMap<UUID, BukkitRunnable> teleportTasks;
    private Connection connection;

    /**
     * Executado quando o plugin é ativado.
     * <p>Este método inicializa o plugin, configura o banco de dados e registra comandos e eventos.</p>
     */
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Home Plugin Ativado!");

        cooldowns = new HashMap<>();
        teleportTasks = new HashMap<>();

        saveDefaultConfig();

        setupDatabase();

        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
    }

    /**
     * Executado quando o plugin é desativado.
     * <p>Este método fecha a conexão com o banco de dados se estiver aberta.</p>
     */
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Home Plugin Desativado!");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura a conexão com o banco de dados usando as informações do arquivo config.yml.
     * <p>Este método verifica os parâmetros de configuração e tenta estabelecer uma conexão com o banco de dados.</p>
     */
    private void setupDatabase() {
        // Obtenha as variáveis de ambiente
        String url = getConfig().getString("database.url");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");

        // Verifique e logue os parâmetros de configuração
        Bukkit.getConsoleSender().sendMessage("Tentando conectar ao banco de dados com URL: " + url);
        Bukkit.getConsoleSender().sendMessage("Usuário do banco de dados: " + user);

        // Adicione uma verificação para garantir que os parâmetros não são nulos ou vazios
        if (url == null || url.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("A URL do banco de dados está vazia!");
            return;
        }
        if (user == null || user.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("O usuário do banco de dados está vazio!");
            return;
        }

        try {
            // Conecte ao banco de dados
            connection = DriverManager.getConnection(url, user, password);
            createTableIfNotExists();
            Bukkit.getConsoleSender().sendMessage("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Falha ao conectar com o banco de dados: " + e.getMessage());
        }
    }

    /**
     * Cria a tabela "homes" no banco de dados se ela não existir.
     * <p>Essa tabela é usada para armazenar as informações de home dos jogadores.</p>
     *
     * @throws SQLException Se ocorrer um erro ao executar a consulta SQL.
     */
    private void createTableIfNotExists() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS homes (" +
                    "player_uuid CHAR(36) NOT NULL, " +
                    "world VARCHAR(255) NOT NULL, " +
                    "x DOUBLE NOT NULL, " +
                    "y DOUBLE NOT NULL, " +
                    "z DOUBLE NOT NULL, " +
                    "PRIMARY KEY (player_uuid))";
            stmt.executeUpdate(sql);
        }
    }

    /**
     * Define a home para um jogador no banco de dados.
     * <p>Se uma home já estiver definida, ela será substituída.</p>
     *
     * @param player O jogador que está definindo a home.
     * @param location A localização da home.
     */
    public void setHome(Player player, Location location) {
        String sql = "REPLACE INTO homes (player_uuid, world, x, y, z) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, location.getWorld().getName());
            stmt.setDouble(3, location.getX());
            stmt.setDouble(4, location.getY());
            stmt.setDouble(5, location.getZ());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtém a home de um jogador a partir do banco de dados.
     *
     * @param player O jogador cuja home será recuperada.
     * @return A localização da home ou {@code null} se a home não estiver definida.
     */
    public Location getHome(Player player) {
        String sql = "SELECT world, x, y, z FROM homes WHERE player_uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String worldName = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                return new Location(Bukkit.getWorld(worldName), x, y, z);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtém o mapa de cooldowns dos jogadores.
     *
     * @return Um mapa onde a chave é o UUID do jogador e o valor é o timestamp do cooldown.
     */
    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    /**
     * Obtém o mapa de tarefas de teletransporte dos jogadores.
     *
     * @return Um mapa onde a chave é o UUID do jogador e o valor é a tarefa de teletransporte associada.
     */
    public HashMap<UUID, BukkitRunnable> getTeleportTasks() {
        return teleportTasks;
    }
}
