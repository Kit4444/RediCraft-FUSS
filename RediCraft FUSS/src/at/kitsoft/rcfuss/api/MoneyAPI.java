package at.kitsoft.rcfuss.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import at.kitsoft.rcfuss.mysql.lb.MySQL;


public class MoneyAPI {
	
	public static double getPocketMoney(UUID uuid) {
		double money = 0.0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT money FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				money = rs.getDouble("money");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return money;
	}
	
	public static double getBankMoney(UUID uuid) {
		double money = 0.0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT bankmoney FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				money = rs.getDouble("bankmoney");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return money;
	}
	
	public static void addPocketMoney(UUID uuid, double amount) {
		double current = getPocketMoney(uuid);
		double newBalance = (current + amount);
		setPocketMoney(uuid, newBalance);
	}
	
	public static void addBankMoney(UUID uuid, double amount) {
		double current = getBankMoney(uuid);
		double newBalance = (current + amount);
		setBankMoney(uuid, newBalance);
	}
	
	//if true, money is deductable, if false, not enough funds to do so.
	public static boolean deductPocketMoney(UUID uuid, double amount) {
		double current = getPocketMoney(uuid);
		if(amount >= current) {
			return false;
		}else {
			double newBalance = (current - amount);
			setPocketMoney(uuid, newBalance);
			return true;
		}
	}
	
	//if true, money is deductable, if false, not enough funds to do so.
	public static boolean deductBankMoney(UUID uuid, double amount) {
		double current = getBankMoney(uuid);
		if(amount >= current) {
			return false;
		}else {
			double newBalance = (current - amount);
			setBankMoney(uuid, newBalance);
			return true;
		}
	}
	
	public static void setPocketMoney(UUID uuid, double amount) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET money = ? WHERE uuid = ?");
			ps.setDouble(1, amount);
			ps.setString(2, uuid.toString().replace("-", ""));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setBankMoney(UUID uuid, double amount) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET bankmoney = ? WHERE uuid = ?");
			ps.setDouble(1, amount);
			ps.setString(2, uuid.toString().replace("-", ""));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasPocketAccount(UUID uuid) {
		return true;
	}
	
	//useless method, as by joining the server the first time
	//the account will be created (pocket & bank).
	public static boolean hasBankAccount(UUID uuid) {
		return true;
	}
	
	public static double getCustomBankInterestRate(UUID uuid) {
		double rate = 0.15;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_interestRate FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				rate = rs.getDouble("tax_interestRate");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}
	
	public static double getCustomMoneyMoveCostRate(UUID uuid) {
		double rate = 0.15;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_moneymoveRate FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				rate = rs.getDouble("tax_moneymoveRate");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}
	
	public static double getServerDefaultBankInterestRate(String server) {
		double rate = 0.15;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_interestRate FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				rate = rs.getDouble("tax_interestRate");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}
	
	public static double getServerDefaultMoneyMoveCostRate(String server) {
		double rate = 0.15;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_moneymoveRate FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				rate = rs.getDouble("tax_moneymoveRate");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}
	
	public enum TaxClass{
		PLUS4("+4"),
		PLUS3("+3"),
		PLUS2("+2"),
		PLUS1("+1"),
		NEUTRAL("0"),
		NEGATIVE1("-1"),
		NEGATIVE2("-2"),
		NEGATIVE3("-3"),
		NEGATIVE4("-4");
		
		private String taxClass;
		
		TaxClass(String taxclass){
			this.taxClass = taxclass;
		}
		
		public String getTaxClass() {
			return taxClass;
		}
	}
	
	public static TaxClass getTaxClass(UUID uuid) {
		TaxClass tc = null;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_class FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				switch(rs.getInt("tax_class")) {
				case 4: tc = TaxClass.PLUS4; break;
				case 3: tc = TaxClass.PLUS3; break;
				case 2: tc = TaxClass.PLUS2; break;
				case 1: tc = TaxClass.PLUS1; break;
				case 0: tc = TaxClass.NEUTRAL; break;
				case -1: tc = TaxClass.NEGATIVE1; break;
				case -2: tc = TaxClass.NEGATIVE2; break;
				case -3: tc = TaxClass.NEGATIVE3; break;
				case -4: tc = TaxClass.NEGATIVE4; break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tc;
	}
	
	public static boolean hasEnoughMoney(double hasToPay, double availableToPay) {
		if(hasToPay >= availableToPay) {
			return false;
		}else {
			return true;
		}
	}
	
	public static double getMoneyTotal(UUID uuid) {
		double total = 0.0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT money,bankmoney FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid.toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				total = rs.getDouble("money");
				total += rs.getDouble("bankmoney");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	public static double getServerMoney(String server) {
		double money = 0.0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT tax_economyMoney FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				money = rs.getDouble("tax_economyMoney");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return money;
	}
	
	public static void setServerMoney(String server, double money) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_serverstats SET tax_economyMoney = ? WHERE servername = ?");
			ps.setDouble(1, money);
			ps.setString(2, server);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addServerMoney(String server, double moneyToAdd) {
		double current = getServerMoney(server);
		double newBalance = (current + moneyToAdd);
		setServerMoney(server, newBalance);
	}
	
	public static void deductServerMoney(String server, double moneyToDeduct) {
		double current = getServerMoney(server);
		double newBalance = (current - moneyToDeduct);
		setServerMoney(server, newBalance);
	}

}
