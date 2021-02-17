
# Set Home &amp; Waypoints

Set Home & waypoint aims **to improve User Experience** and **intuitive CLI** (Command Line Interface) for teleportation commands. This mod is **available in two languages**, French and English. **3 main commands** give you the ability to: create your home (`/sethome`); teleport to your home (`/home` or shortcut); create multiple waypoints to teleport on them (`/wp`). You can also configurate the mod if you want to with, CLI for servers and mod options for solo.

**All commands use autocomplete** to guide you through the mod. :)

![Picture: Waypoints](https://res.cloudinary.com/dodgeman/image/upload/v1613479367/shw-new/client-wp_xi8tbj.png)

# Installation

First of all, [Download Set Home & Waypoints on CurseForge](https://www.curseforge.com/minecraft/mc-mods/set-home-waypoints)

**Both sides need the mod in mods directory !**

Needed in the client side to translate messages in your locale and be able to use shortcut to teleport to your home.

# Configuration

Set Home & Waypoints allows you to change settings (cooldown, travel through dimension…).

## In Solo

If you play in solo, you can configure the mod using the **mod config interface**.

![Picture: Solo Configuration](https://res.cloudinary.com/dodgeman/image/upload/v1613476667/shw-new/client-config_fqmy69.png)
![Picture: Solo Configuration Home](https://res.cloudinary.com/dodgeman/image/upload/v1613476667/shw-new/client-config-home_du2ttw.png)
![Picture: Solo Configuration Waypoints ](https://res.cloudinary.com/dodgeman/image/upload/v1613476667/shw-new/client-config-wp_wgo9td.png)

## For Server

If you play in server/client config you can change settings using CLI or edit shw.cfg.

![Picture: Server Configuration Waypoints](https://res.cloudinary.com/dodgeman/image/upload/v1613480395/shw-new/server-wp-help_odyh6o.png)
![Picture: Server Configuration Cooldown of Waypoints](https://res.cloudinary.com/dodgeman/image/upload/v1613480393/shw-new/server-wp-cooldown_neyln7.png)

When updating configuration, **players are automatically informed**.

![Picture: Feedback to player after updating Configuration](https://res.cloudinary.com/dodgeman/image/upload/v1613482646/shw-new/wp-client-info-update_qxpqn0.png)

If you want to disable wp or home command **you must edit the config file shw.cfg**.

![Picture: File to edit if you want to disable Home or/and Waypoints](https://res.cloudinary.com/dodgeman/image/upload/v1613482647/shw-new/shw-config-shw.cfg_hasn3x.png)

# Usage

After all your configuration done, intuitive commands will help you to familiarize with Set Home & Waypoints. 

- `/sethome` => Create your home point,
- `/home` => Teleport to your home,
- `/wp` => Manage your waypoints.

## Home & Set Home

To use home command, you will have to set your home first (/sethome), and then teleport to it when ever you want (/home or shortcut).

![Picutre: Set Home command](https://res.cloudinary.com/dodgeman/image/upload/v1613479575/shw-new/client-set-home_nopuwr.png)
![Picture: Use Home command](https://res.cloudinary.com/dodgeman/image/upload/v1613479573/shw-new/client-home_iiieaw.png)

If you want to **use shortcut** to teleport to you home you can **use 'P' by default**.

You can change it in keyboard settings.

![Picture: Use Home command with shortcut](https://res.cloudinary.com/dodgeman/image/upload/v1613480391/shw-new/client-config-shortcut_ihucri.png)

**To show configuration**, you can use the following commands.

![Picture: Client Home Help](https://res.cloudinary.com/dodgeman/image/upload/v1613480413/shw-new/client-home-help_fcv9oy.png)
![Picture: Client Show Cooldown](https://res.cloudinary.com/dodgeman/image/upload/v1613480406/shw-new/client-home-cooldown_uo4kzv.png)

## Waypoints

With Set Home & Waypoints, you can manage additional points. Like Home command, **it can be configured and disabled**.

![Picture: Waypoint help](https://res.cloudinary.com/dodgeman/image/upload/v1613479367/shw-new/client-wp_xi8tbj.png)

To create a waypoint execute `/wp set <name>`.

![Picture: Waypoint set example](https://res.cloudinary.com/dodgeman/image/upload/v1613480419/shw-new/client-wp-set_opd8dz.png)

To create a waypoint execute `/wp use <name>`.

![Picture: Waypoint use example](https://res.cloudinary.com/dodgeman/image/upload/v1613480405/shw-new/client-wp-use_cw0pah.png)

To create a waypoint execute `/wp remove <name>`.

![Picture: Waypoint Remove example](https://res.cloudinary.com/dodgeman/image/upload/v1613480415/shw-new/wp-client-remove-with-message-undo_fuvbhr.png)

If you did wrong execution and **didn't execute new wp command** you can undo your last wp, `/wp undo`.

![Picture: Undo Waypoint](https://res.cloudinary.com/dodgeman/image/upload/v1613480419/shw-new/wp-client-undo_knweki.png)

If you didn't respect, you wont be able to recover your waypoint so, **double check before execution** !

![Picture: Waypoint Undo fail](https://res.cloudinary.com/dodgeman/image/upload/v1613480418/shw-new/wp-client-undo-fail_t4glri.png)

You can list your current waypoints with `/wp list`.

Counter uses colors to show state of your available storage.

![Picture: Waypoint List](https://res.cloudinary.com/dodgeman/image/upload/v1613480410/shw-new/client-wp-list_l6lvps.png)

Other commands like `/wp clear` can be execute or show cooldown. I let you try it in game :)

I hope you'll enjoy **Set Home & Waypoints** !

Don't hesitate to leave a comment if you have any issue.
