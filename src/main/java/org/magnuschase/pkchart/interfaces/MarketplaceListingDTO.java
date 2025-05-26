package org.magnuschase.pkchart.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import org.magnuschase.pkchart.entity.MarketplaceListing;
import org.magnuschase.pkchart.model.CardCondition;
import org.magnuschase.pkchart.model.MarketplaceListingType;

@Data
public class MarketplaceListingDTO {
    private Long id;
    private String contactInfo;
    private LocalDateTime createdAt;
    private CardCondition condition;
    private CardDTO card;
    private MarketplaceListingType type;
    private BigDecimal price;

    public static MarketplaceListingDTO fromEntity(MarketplaceListing listing) {
        MarketplaceListingDTO marketpliceListingDto = new MarketplaceListingDTO();
        marketpliceListingDto.setId(listing.getId());
        marketpliceListingDto.setCreatedAt(listing.getCreatedAt());
        marketpliceListingDto.setPrice(listing.getPrice());
        marketpliceListingDto.setType(listing.getType());
        marketpliceListingDto.setContactInfo(listing.getContactInfo());
        marketpliceListingDto.setCondition(listing.getCondition());
        marketpliceListingDto.setCard(CardDTO.fromEntity(listing.getCard()));

        return marketpliceListingDto;
    }
}
