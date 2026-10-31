# Vindicator's Slash Blade

让任意生物真正会「用」拔刀剑（SlashBlade: Resharped）。原版里怪物拿刀只会平砍，这个模组把拔刀剑的战斗系统接进怪物 AI——连段、剑技、空中连段、瞬步、以及 6 级解锁的召唤剑（螺旋剑 + 追踪飞剑），同时保证它们不会误伤队友。

## 前置依赖

| 模组 | 版本 |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.228+ |
| SlashBlade: Resharped | 2.1.9-1.21.1+ |
| SBR Core | 1.5.2-hotfix1+ |
| playerAnimator | 2.0.4+1.21.1+ |

客户端和服务端都要装。

## 快速开始

配置文件在 `config/vindicators_slash_blade-common.toml`（服务端配置）：

```toml
bladerWhitelist = ["minecraft:vindicator, 6"]   # 谁会用拔刀剑 + 等级（1–6）
bornWithBladeWhitelist = ["minecraft:vindicator"]  # 谁生成时手里就有刀
bornWithBladeChance = 1.0
bladeDropChance = 0.0
showEnderParticles = true
usePlayerAnimator = true   # 是否播放刀剑动作动画（低配机器可关）
```

## 等级（1–6）

| 等级 | 大致能力 |
|---|---|
| 1 | 基础连段 |
| 2 | + 强力 |
| 3 | + 迅冲斩、跃升斩跳 |
| 4 | + 剑技、虚无刀界、瞬步三连 |
| 5 | + Just 剑技 |
| 6 | + 幻影刀（螺旋剑环绕 + 飞剑追踪） |

## 主要特性

- 配置白名单决定谁会用刀、几级；不用数据包、不用写代码。
- 生来带刀：从注册表里**所有**拔刀剑中随机抽一把。
- 友军保护：范围剑气、召唤剑都不会误伤队友；对玩家友好的中立生物也不会打玩家。
- 完整动作动画（playerAnimator），支持人形 / 灾厄村民 / 村民三类原版模型，其他模型自动禁用动画以免冲突。
- 怪物用刀不耗耐久。

完整说明见 [docs/modrinth-description.zh.md](docs/modrinth-description.zh.md)（中文）与 [docs/modrinth-description.en.md](docs/modrinth-description.en.md)（英文）。

## 构建

需要 JDK 21：

```bash
./gradlew build
```

产物在 `build/libs/`。

## 许可

MIT
