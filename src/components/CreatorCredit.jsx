export default function CreatorCredit({ compact = false }) {
  return (
    <div
      className={`creator-credit${compact ? " creator-credit-compact" : ""}`}
    >
      {!compact && (
        <span className="creator-monogram" aria-hidden="true">
          AR
        </span>
      )}
      <p>
        <span>Designed &amp; engineered by</span> <strong>Agnibha Ray</strong>
      </p>
    </div>
  );
}
