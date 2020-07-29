package jp.co.internous.ocean.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import jp.co.internous.ocean.model.domain.TblCart;
import jp.co.internous.ocean.model.domain.dto.CartDto;
import jp.co.internous.ocean.model.form.CartForm;
import jp.co.internous.ocean.model.mapper.TblCartMapper;
import jp.co.internous.ocean.model.session.LoginSession;

@Controller
@RequestMapping("/ocean/cart")
public class CartController {
	
	@Autowired
	TblCartMapper tblCartMapper;
	
	@Autowired
	LoginSession loginSession;
	
	@Autowired
	Gson gson = new Gson();

	@RequestMapping("/")
	public String index(Model m) {
		// loginSessionからユーザーの情報を取得
		// ログインの状態によって変数userIdを代入
		int userId = loginSession.getLogined() ? loginSession.getUserId() : loginSession.getTmpUserId();
		
		// userIdに紐づくカート情報を取得し表示する
		List<CartDto> cartList = tblCartMapper.findByUserId(userId);
		m.addAttribute("cartList", cartList);
		m.addAttribute("loginSession", loginSession);
		
		return "cart";
	}
	
	@RequestMapping("/add")
	public String add(CartForm cf, Model m) {
		// loginSessionからユーザーの情報を取得
		// ログインの状態によって変数userIdを代入
		int userId = loginSession.getLogined() ? loginSession.getUserId() : loginSession.getTmpUserId();

		// ユーザーIDと商品IDで合致するものがあるか確認
		List<CartDto> cartInfo = tblCartMapper.findByUserIdAndProductId(userId, cf.getProductId());
		
		// 条件分岐 -- case true -- 重複がある場合
		if (cartInfo != null && cartInfo.size() > 0) {
			
			// すでにカート内にある商品IDの個数を更新する
			CartDto cd = cartInfo.get(0);
			int sumCount = cd.getProductCount() + cf.getProductCount();
			
			// 対象商品のDBの情報を更新
			tblCartMapper.update(userId, cf.getProductId(), sumCount);
			
		}else { // --case false -- 重複がない場合
			
			// CartFormから送られてきた情報をTblCartに保管するためインスタンス化
			TblCart c = new TblCart();
						
			// インスタンスに送られてきた情報を格納
			c.setUserId(userId);
			c.setProductId(cf.getProductId());
			c.setProductCount(cf.getProductCount());
			
			// DBに格納
			tblCartMapper.insert(c);
		}
		
		// ビューに表示を返す
		List<CartDto> cartList = tblCartMapper.findByUserId(userId);
		m.addAttribute("cartList", cartList);
		m.addAttribute("loginSession", loginSession);
		
		// cartの表示処理に渡す
		return "cart";
	}
	
	@ResponseBody
	@PostMapping("/delete")
	public String delete(@RequestBody CartForm cf) {
		// チェックされた商品を削除する機能,ajaxでの処理
		int id = cf.getId();
		
		// 引数の値が妥当かどうかの例外処理
		try {			
			tblCartMapper.deleteById(id);
		}catch (IllegalArgumentException e) {
			return "-1";
		}
		return "1";
	}
}