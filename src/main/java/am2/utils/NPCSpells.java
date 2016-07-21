package am2.utils;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import am2.spell.component.Blind;
import am2.spell.component.Blink;
import am2.spell.component.Dispel;
import am2.spell.component.Drown;
import am2.spell.component.FireDamage;
import am2.spell.component.FrostDamage;
import am2.spell.component.Heal;
import am2.spell.component.Ignition;
import am2.spell.component.Knockback;
import am2.spell.component.LifeDrain;
import am2.spell.component.LightningDamage;
import am2.spell.component.MagicDamage;
import am2.spell.component.ManaDrain;
import am2.spell.component.MeltArmor;
import am2.spell.component.Nauseate;
import am2.spell.component.PhysicalDamage;
import am2.spell.component.RandomTeleport;
import am2.spell.component.ScrambleSynapses;
import am2.spell.component.Silence;
import am2.spell.component.Slow;
import am2.spell.component.WateryGrave;
import am2.spell.modifier.Damage;
import am2.spell.modifier.Radius;
import am2.spell.modifier.Speed;
import am2.spell.shape.AoE;
import am2.spell.shape.Projectile;
import am2.spell.shape.Rune;
import am2.spell.shape.Self;
import am2.spell.shape.Wave;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NPCSpells{
	public static final NPCSpells instance = new NPCSpells();

	public final ItemStack lightMage_DiminishedAttack;
	public final ItemStack lightMage_NormalAttack;
	public final ItemStack lightMage_AugmentedAttack;

	public final ItemStack darkMage_DiminishedAttack;
	public final ItemStack darkMage_NormalAttack;
	public final ItemStack darkMage_AugmentedAttack;

	public final ItemStack enderGuardian_enderWave;
	public final ItemStack enderGuardian_enderBolt;
	public final ItemStack enderGuardian_enderTorrent;
	public final ItemStack enderGuardian_otherworldlyRoar;

	public final ItemStack dispel;
	public final ItemStack blink;
	public final ItemStack arcaneBolt;
	public final ItemStack meltArmor;
	public final ItemStack waterBolt;
	public final ItemStack fireBolt;
	public final ItemStack healSelf;
	public final ItemStack nauseate;
	public final ItemStack lightningRune;
	public final ItemStack scrambleSynapses;

	private NPCSpells(){
		lightMage_DiminishedAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new PhysicalDamage()), new NBTTagCompound());

		lightMage_NormalAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new FrostDamage(), new Slow()), new NBTTagCompound());

		lightMage_AugmentedAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new MagicDamage(), new Blind(), new Damage()), new NBTTagCompound());

		darkMage_DiminishedAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new MagicDamage()), new NBTTagCompound());

		darkMage_NormalAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new FireDamage(), new Ignition()), new NBTTagCompound());

		darkMage_AugmentedAttack = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new LightningDamage(), new Knockback(), new Damage()), new NBTTagCompound());

		enderGuardian_enderWave = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Wave(), new Radius(), new Radius(), new MagicDamage(), new Knockback()), new NBTTagCompound());

		enderGuardian_enderBolt = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new MagicDamage(), new RandomTeleport(), new Damage()), new NBTTagCompound());

		enderGuardian_otherworldlyRoar = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new AoE(), new Blind(), new Silence(), new Knockback(), new Radius(), new Radius(), new Radius(), new Radius(), new Radius()), new NBTTagCompound());

		enderGuardian_enderTorrent = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new Silence(), new Knockback(), new Speed(), new AoE(), new ManaDrain(), new LifeDrain()), new NBTTagCompound());

		dispel = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Self(), new Dispel()), new NBTTagCompound());

		blink = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Self(), new Blink()), new NBTTagCompound());

		arcaneBolt = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new MagicDamage()), new NBTTagCompound());

		meltArmor = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new MeltArmor()), new NBTTagCompound());

		waterBolt = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new WateryGrave(), new Drown()), new NBTTagCompound());

		fireBolt = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new FireDamage(), new Ignition()), new NBTTagCompound());

		healSelf = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Self(), new Heal()), new NBTTagCompound());

		nauseate = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new Nauseate(), new ScrambleSynapses()), new NBTTagCompound());

		lightningRune = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new Rune(), new AoE(), new LightningDamage(), new Damage()), new NBTTagCompound());

		scrambleSynapses = SpellUtils.createSpellStack(new ArrayList<>(), Lists.newArrayList(new Projectile(), new LightningDamage(), new AoE(), new ScrambleSynapses(), new Radius(), new Radius(), new Radius(), new Radius(), new Radius()), new NBTTagCompound());
	}
}
