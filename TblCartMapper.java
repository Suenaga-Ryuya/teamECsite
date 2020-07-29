package jp.co.internous.ocean.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.ocean.model.domain.TblCart;
import jp.co.internous.ocean.model.domain.dto.CartDto;

@Mapper
public interface TblCartMapper {

	// ユーザーによるカート情報を取得するSQL文
	// TblCartMapper.xmlに詳細セレクト文を記述(簡潔な内容ではないため)
	List<CartDto> findByUserId(@Param("userId") int userId);
	
	// 重複商品があるかどうかを確認するためのSQL文
	@Select("SELECT * FROM tbl_cart WHERE user_id = #{userId} AND product_id = #{productId}")
	List<CartDto> findByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);
	
	// 重複商品が存在する場合に個数を更新するSQL文
	@Update("UPDATE tbl_cart SET product_count = #{productCount}, updated_at = now() WHERE user_id = #{userId} AND product_id = #{productId}")
	void update(@Param("userId") int userId, @Param("productId") int productId, @Param("productCount") int productCount);
	
	// カート情報を切り替えるための簡易SQL文
	@Update("UPDATE tbl_cart SET user_id = #{loginedUserId}, updated_at = now() WHERE user_id = #{unloginUserId}")
	void updateUserId(@Param("loginedUserId") int loginedUserId, @Param("unloginUserId") int unloginUserId);
	
	// フォームから送られてきた情報をもとにカート情報を追加するSQL文
	@Insert("INSERT INTO tbl_cart (user_id, product_id, product_count, created_at, updated_at) VALUES (#{userId}, #{productId}, #{productCount}, now(), now())")
	@Options(useGeneratedKeys=true, keyProperty="id")
	void insert(TblCart tblCart);
	
	// チェックされた商品をカートから削除するためのSQL文
	@Delete("DELETE FROM tbl_cart WHERE id = #{id}")
	boolean deleteById(int id);
	
	// 追加機能...
	// ログインしたユーザーでのカート情報の破棄するSQL文
	@Delete("DELETE FROM tbl_cart WHERE user_id = #{userId}")
	int deleteByTmpUserId(int userId);
	
	// ログインした時の仮ユーザーとユーザーIdの切り替え時にカート情報をまとめるために使うSQL文
	@Delete("DELETE FROM tbl_cart WHERE user_id = #{userId} AND product_id = #{productId}")
	boolean deleteByTmpUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);
}