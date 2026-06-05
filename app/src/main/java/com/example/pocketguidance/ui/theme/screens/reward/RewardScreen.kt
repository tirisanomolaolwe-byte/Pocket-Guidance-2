package com.example.pocketguidance.ui.theme.screens.reward

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.data.model.Reward
import com.example.pocketguidance.viewmodel.RewardViewModel

@Composable
fun RewardScreen(
    viewModel: RewardViewModel,
    userId: Int
) {
    LaunchedEffect(userId) { viewModel.load(userId) }

    val rewards    = viewModel.rewards
    val xp         = viewModel.xp
    val nextLevelXp = viewModel.nextLevelXp
    val level      = viewModel.level

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Title─
        Text("Rewards",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Earn badges by hitting your budget goals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(20.dp))

        //XP /level card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors   = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Level $level",
                            style      = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Budget Champion",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Text("⭐ $xp XP",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress   = { (xp.toFloat() / nextLevelXp).coerceIn(0f, 1f) },
                    modifier   = Modifier.fillMaxWidth().height(8.dp),
                    color      = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface
                )
                Spacer(Modifier.height(4.dp))
                Text("$xp / $nextLevelXp XP to Level ${level + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        Spacer(Modifier.height(20.dp))

        // earned badges ──
        Text("Earned badges (${rewards.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        if (rewards.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.padding(24.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Text("No badges yet — keep budgeting!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(rewards, key = { it.id }) { reward ->
                    BadgeCard(reward = reward, earned = true)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // locked badges (always shown so user knows what to aim for)
        Text("Badges to unlock",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        val earnedTitles = rewards.map { it.title }.toSet()
        val allBadges = listOf(
            "Saver Badge"        to "Spend less than R1,000 in a month",
            "On Target"          to "Stay within budget for a full month",
            "7-Day Streak"       to "Log expenses 7 days in a row",
            "30-Day Streak"      to "Log expenses 30 days in a row",
            "Budget Master"      to "Stay under budget for 3 months",
            "First Expense"      to "Log your very first expense",
            "Category Pro"       to "Create 5 or more categories",
            "Zero Overspend"     to "Never exceed a category limit in a month"
        )

        val locked = allBadges.filter { it.first !in earnedTitles }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            locked.forEach { (title, desc) ->
                LockedBadgeCard(title = title, description = desc)
            }
        }
    }
}

@Composable
private fun BadgeCard(reward: Reward, earned: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(reward.title.take(2), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(reward.title.drop(3),
                    style      = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium)
                Text("Earned ${reward.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Composable
private fun LockedBadgeCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lock, contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
