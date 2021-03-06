package com.projectkorra.probending.command.team;

import com.projectkorra.probending.PBMethods;
import com.projectkorra.probending.command.Commands;
import com.projectkorra.probending.command.PBCommand;
import com.projectkorra.probending.objects.Team;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class KickCommand extends PBCommand {
	
	public KickCommand() {
		super ("team-kick", "/pb team kick <Player>", "Kick a player from your team.", new String[] {"kick", "k"}, true, Commands.teamaliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!isPlayer(sender) || !hasTeamPermission(sender) || !correctLength(sender, args.size(), 2, 2)) {
			return;
		}
		
		UUID uuid = ((Player) sender).getUniqueId();

		Team team = (PBMethods.getPlayerTeam(uuid));
		if (team == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotInTeam);
			return;
		}
		if (!team.isOwner(uuid)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.NotOwnerOfTeam);
			return;
		}
		String playerName = args.get(1);
		if (playerName.equals(sender.getName())) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.CantBootFromOwnTeam);
			return;
		}
		OfflinePlayer p3 = Bukkit.getOfflinePlayer(args.get(1));
		Team playerTeam = null;
		String playerElement = null;

		if (p3 != null) {
			playerElement = PBMethods.getPlayerElementInTeam(p3.getUniqueId(), team.getName());
			playerTeam = PBMethods.getPlayerTeam(p3.getUniqueId());
		}

		if (playerTeam == null) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotOnThisTeam);
			return;
		}
		if (!playerTeam.equals(team)) {
			sender.sendMessage(PBMethods.Prefix + PBMethods.PlayerNotOnThisTeam);
			return;
		}
		team.removePlayer(p3.getUniqueId());
		Player player = Bukkit.getPlayer(playerName);
		if (player != null) {
			player.sendMessage(PBMethods.Prefix + PBMethods.YouHaveBeenBooted.replace("%team", team.getName()));
		}
		for (Player player2: Bukkit.getOnlinePlayers()) {
			if (PBMethods.getPlayerTeam(player2.getUniqueId()) == null) continue;
			if (PBMethods.getPlayerTeam(player2.getUniqueId()).equals(team.getName())) {
				player2.sendMessage(PBMethods.Prefix + PBMethods.PlayerHasBeenBooted.replace("%player", playerName).replace("%team", team.getName()));
			}
		}
		return;
	}
}