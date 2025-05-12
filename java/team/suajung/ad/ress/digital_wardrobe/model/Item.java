package team.suajung.ad.ress.digital_wardrobe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "UserItem")
public class Item {

    @Id
    private String id;
    private String userId;
    private String wardrobeId;
    private String image_url;
    
    private String type;

    private String color;
    private List<Double> color_embedding;

    private String pattern;
    private List<Double> pattern_embedding;

    private String neckline;
    private List<Double> neckline_embedding;

    private String skirt_type;
    private List<Double> skirt_type_embedding;

    private String fastening_method;
    private List<Double> fastening_method_embedding;

    private String pants_fit;
    private List<Double> pants_fit_embedding;

    private String top_type;
    private List<Double> top_type_embedding;

    private String sleeve_type;
    private List<Double> sleeve_type_embedding;

    private String skirt_fit;
    private List<Double> skirt_fit_embedding;

    private List<String> fit;
    private List<Double> fit_embedding;

    private List<String> style;
    private List<Double> style_embedding;

    private List<String> tpo;
    private List<Double> tpo_embedding;

    private List<String> details;
    private List<Double> details_embedding;

    private List<String> vibe;
    private List<Double> vibe_embedding;

    private List<String> seasons;
    private List<Double> seasons_embedding;

    private List<String> texture;
    private List<Double> texture_embedding;

    private List<String> thickness;
    private List<Double> thickness_embedding;

    private String bottom_length_type;
    private List<Double> bottom_length_type_embedding;

    private String category;
    private List<Double> category_embedding;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWardrobeId() {
		return wardrobeId;
	}
	public void setWardrobeId(String wardrobeId) {
		this.wardrobeId = wardrobeId;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public List<Double> getColor_embedding() {
		return color_embedding;
	}
	public void setColor_embedding(List<Double> color_embedding) {
		this.color_embedding = color_embedding;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public List<Double> getPattern_embedding() {
		return pattern_embedding;
	}
	public void setPattern_embedding(List<Double> pattern_embedding) {
		this.pattern_embedding = pattern_embedding;
	}
	public String getNeckline() {
		return neckline;
	}
	public void setNeckline(String neckline) {
		this.neckline = neckline;
	}
	public List<Double> getNeckline_embedding() {
		return neckline_embedding;
	}
	public void setNeckline_embedding(List<Double> neckline_embedding) {
		this.neckline_embedding = neckline_embedding;
	}
	public String getSkirt_type() {
		return skirt_type;
	}
	public void setSkirt_type(String skirt_type) {
		this.skirt_type = skirt_type;
	}
	public List<Double> getSkirt_type_embedding() {
		return skirt_type_embedding;
	}
	public void setSkirt_type_embedding(List<Double> skirt_type_embedding) {
		this.skirt_type_embedding = skirt_type_embedding;
	}
	public String getFastening_method() {
		return fastening_method;
	}
	public void setFastening_method(String fastening_method) {
		this.fastening_method = fastening_method;
	}
	public List<Double> getFastening_method_embedding() {
		return fastening_method_embedding;
	}
	public void setFastening_method_embedding(List<Double> fastening_method_embedding) {
		this.fastening_method_embedding = fastening_method_embedding;
	}
	public String getPants_fit() {
		return pants_fit;
	}
	public void setPants_fit(String pants_fit) {
		this.pants_fit = pants_fit;
	}
	public List<Double> getPants_fit_embedding() {
		return pants_fit_embedding;
	}
	public void setPants_fit_embedding(List<Double> pants_fit_embedding) {
		this.pants_fit_embedding = pants_fit_embedding;
	}
	public String getTop_type() {
		return top_type;
	}
	public void setTop_type(String top_type) {
		this.top_type = top_type;
	}
	public List<Double> getTop_type_embedding() {
		return top_type_embedding;
	}
	public void setTop_type_embedding(List<Double> top_type_embedding) {
		this.top_type_embedding = top_type_embedding;
	}
	public String getSleeve_type() {
		return sleeve_type;
	}
	public void setSleeve_type(String sleeve_type) {
		this.sleeve_type = sleeve_type;
	}
	public List<Double> getSleeve_type_embedding() {
		return sleeve_type_embedding;
	}
	public void setSleeve_type_embedding(List<Double> sleeve_type_embedding) {
		this.sleeve_type_embedding = sleeve_type_embedding;
	}
	public String getSkirt_fit() {
		return skirt_fit;
	}
	public void setSkirt_fit(String skirt_fit) {
		this.skirt_fit = skirt_fit;
	}
	public List<Double> getSkirt_fit_embedding() {
		return skirt_fit_embedding;
	}
	public void setSkirt_fit_embedding(List<Double> skirt_fit_embedding) {
		this.skirt_fit_embedding = skirt_fit_embedding;
	}
	public List<String> getFit() {
		return fit;
	}
	public void setFit(List<String> fit) {
		this.fit = fit;
	}
	public List<Double> getFit_embedding() {
		return fit_embedding;
	}
	public void setFit_embedding(List<Double> fit_embedding) {
		this.fit_embedding = fit_embedding;
	}
	public List<String> getStyle() {
		return style;
	}
	public void setStyle(List<String> style) {
		this.style = style;
	}
	public List<Double> getStyle_embedding() {
		return style_embedding;
	}
	public void setStyle_embedding(List<Double> style_embedding) {
		this.style_embedding = style_embedding;
	}
	public List<String> getTpo() {
		return tpo;
	}
	public void setTpo(List<String> tpo) {
		this.tpo = tpo;
	}
	public List<Double> getTpo_embedding() {
		return tpo_embedding;
	}
	public void setTpo_embedding(List<Double> tpo_embedding) {
		this.tpo_embedding = tpo_embedding;
	}
	public List<String> getDetails() {
		return details;
	}
	public void setDetails(List<String> details) {
		this.details = details;
	}
	public List<Double> getDetails_embedding() {
		return details_embedding;
	}
	public void setDetails_embedding(List<Double> details_embedding) {
		this.details_embedding = details_embedding;
	}
	public List<String> getVibe() {
		return vibe;
	}
	public void setVibe(List<String> vibe) {
		this.vibe = vibe;
	}
	public List<Double> getVibe_embedding() {
		return vibe_embedding;
	}
	public void setVibe_embedding(List<Double> vibe_embedding) {
		this.vibe_embedding = vibe_embedding;
	}
	public List<String> getSeasons() {
		return seasons;
	}
	public void setSeasons(List<String> seasons) {
		this.seasons = seasons;
	}
	public List<Double> getSeasons_embedding() {
		return seasons_embedding;
	}
	public void setSeasons_embedding(List<Double> seasons_embedding) {
		this.seasons_embedding = seasons_embedding;
	}
	public List<String> getTexture() {
		return texture;
	}
	public void setTexture(List<String> texture) {
		this.texture = texture;
	}
	public List<Double> getTexture_embedding() {
		return texture_embedding;
	}
	public void setTexture_embedding(List<Double> texture_embedding) {
		this.texture_embedding = texture_embedding;
	}
	public List<String> getThickness() {
		return thickness;
	}
	public void setThickness(List<String> thickness) {
		this.thickness = thickness;
	}
	public List<Double> getThickness_embedding() {
		return thickness_embedding;
	}
	public void setThickness_embedding(List<Double> thickness_embedding) {
		this.thickness_embedding = thickness_embedding;
	}
	public String getBottom_length_type() {
		return bottom_length_type;
	}
	public void setBottom_length_type(String bottom_length_type) {
		this.bottom_length_type = bottom_length_type;
	}
	public List<Double> getBottom_length_type_embedding() {
		return bottom_length_type_embedding;
	}
	public void setBottom_length_type_embedding(List<Double> bottom_length_type_embedding) {
		this.bottom_length_type_embedding = bottom_length_type_embedding;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Double> getCategory_embedding() {
		return category_embedding;
	}
	public void setCategory_embedding(List<Double> category_embedding) {
		this.category_embedding = category_embedding;
	}
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    
}