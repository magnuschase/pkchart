package org.magnuschase.pkchart.service;

import org.magnuschase.pkchart.entity.CardPrice;
import org.magnuschase.pkchart.entity.User;
import org.magnuschase.pkchart.entity.UserPortfolioEntry;
import org.magnuschase.pkchart.interfaces.PortfolioAddCardRequestDTO;
import org.magnuschase.pkchart.interfaces.PortfolioDTO;
import org.magnuschase.pkchart.interfaces.PortfolioEntryDTO;
import org.magnuschase.pkchart.repository.CardPriceRepository;
import org.magnuschase.pkchart.repository.CardRepository;
import org.magnuschase.pkchart.repository.UserPortfolioEntryRepository;
import org.magnuschase.pkchart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    @Autowired
    private UserPortfolioEntryRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardPriceRepository cardPriceRepository;

    public PortfolioDTO getPortfolioForUser(UserDetails userDetails) {
        User user =
                userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<UserPortfolioEntry> entries = portfolioRepository.findAllByUserId(user.getId());
        List<PortfolioEntryDTO> cards = entries.stream()
                .map(entry -> {
                    Optional<CardPrice> latestPriceRecord = cardPriceRepository.findFirstByCardOrderByDateDesc(entry.getCard());
                    BigDecimal currentPrice = latestPriceRecord
                            .map(CardPrice::getPrice)
                            .orElse(BigDecimal.ZERO);
                    return PortfolioEntryDTO.fromEntity(entry, currentPrice);
                })
                .toList();
        BigDecimal totalPrice = cards.stream()
                .map(PortfolioEntryDTO::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PortfolioDTO dto = new PortfolioDTO();
        dto.setCards(cards);
        dto.setTotalPrice(totalPrice);
        return dto;
    }

    public PortfolioDTO addCard(UserDetails userDetails, PortfolioAddCardRequestDTO request, Long cardId) {
        User user =
                userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!cardRepository.existsById(cardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
        }

        UserPortfolioEntry entry = new UserPortfolioEntry();
        entry.setUser(user);
        entry.setCard(cardRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")));
        entry.setCondition(request.getCondition());
        entry.setQuantity(request.getQuantity());
        portfolioRepository.save(entry);

        return getPortfolioForUser(userDetails);
    }

    public PortfolioDTO removeCard(UserDetails userDetails, Long entryId) {
        User user =
                userRepository
                        .findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserPortfolioEntry entry = portfolioRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to remove this entry");
        }
        portfolioRepository.delete(entry);

        return getPortfolioForUser(userDetails);
    }
}
