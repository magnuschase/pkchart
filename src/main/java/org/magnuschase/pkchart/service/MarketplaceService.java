package org.magnuschase.pkchart.service;

import org.magnuschase.pkchart.entity.Card;
import org.magnuschase.pkchart.entity.MarketplaceListing;
import org.magnuschase.pkchart.entity.User;
import org.magnuschase.pkchart.entity.UserPortfolioEntry;
import org.magnuschase.pkchart.interfaces.CardMarketplaceBuyRequestDTO;
import org.magnuschase.pkchart.interfaces.CardMarketplaceSellRequestDTO;
import org.magnuschase.pkchart.interfaces.MarketplaceListingDTO;
import org.magnuschase.pkchart.model.MarketplaceListingType;
import org.magnuschase.pkchart.repository.CardRepository;
import org.magnuschase.pkchart.repository.MarketplaceListingRepository;
import org.magnuschase.pkchart.repository.UserPortfolioEntryRepository;
import org.magnuschase.pkchart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MarketplaceService {

    @Autowired
    private MarketplaceListingRepository marketplaceRepository;
    @Autowired private UserPortfolioEntryRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    public MarketplaceListingDTO sellCard(
            UserDetails userDetails, CardMarketplaceSellRequestDTO request) {
        Long portfolioEntryId = request.getPortfolioEntryId();
        User user =
                userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        UserPortfolioEntry entry =
                portfolioRepository
                        .findById(portfolioEntryId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not own this card. Please check your portfolio.");
        }


        MarketplaceListing marketplaceListing = new MarketplaceListing();
        marketplaceListing.setCard(entry.getCard());
        marketplaceListing.setUser(user);
        marketplaceListing.setPrice(BigDecimal.valueOf(request.getPrice()));
        marketplaceListing.setCondition(entry.getCondition());
        marketplaceListing.setContactInfo(user.getEmail());
        marketplaceListing.setType(MarketplaceListingType.SELL);

        MarketplaceListing result = marketplaceRepository.save(marketplaceListing);
        return MarketplaceListingDTO.fromEntity(result);
    }

    public MarketplaceListingDTO buyCard(
            UserDetails userDetails, CardMarketplaceBuyRequestDTO request) {
        Long cardId = request.getCardId();
        User user =
                userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

        MarketplaceListing marketplaceListing = new MarketplaceListing();
        marketplaceListing.setCard(card);
        marketplaceListing.setUser(user);
        marketplaceListing.setPrice(BigDecimal.valueOf(request.getPrice()));
        marketplaceListing.setCondition(request.getCondition());
        marketplaceListing.setContactInfo(user.getEmail());
        marketplaceListing.setType(MarketplaceListingType.BUY);

        MarketplaceListing result = marketplaceRepository.save(marketplaceListing);
        return MarketplaceListingDTO.fromEntity(result);
    }

    public List<MarketplaceListingDTO> getAllListings() {
        List<MarketplaceListing> listings = marketplaceRepository.findAll();
        return listings.stream().map(MarketplaceListingDTO::fromEntity).toList();
    }

}
